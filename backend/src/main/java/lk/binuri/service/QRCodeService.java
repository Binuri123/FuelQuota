package lk.binuri.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class QRCodeService {

    private static final int QR_WIDTH = 200;
    private static final int QR_HEIGHT = 200;

    public void generateQRCodeToFile(String vehicleNo, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(vehicleNo, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);

        Path path = Paths.get(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public byte[] generateQRCode(String vehicleNo) throws WriterException, IOException {

        String text = vehicleNo + "|" + UUID.randomUUID();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream.toByteArray();
    }

    public String decodeVehicleNoFromBase64QRCode(String base64Image) throws IOException, NotFoundException, ChecksumException, FormatException {
        // Step 1: Remove data URI prefix if present
        if (base64Image.startsWith("data:image")) {
            int commaIndex = base64Image.indexOf(",");
            base64Image = base64Image.substring(commaIndex + 1);
        }

        // Step 2: Decode Base64
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        // Step 3: Convert bytes to BufferedImage
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(bis);
        if (bufferedImage == null) {
            throw new IOException("Could not decode image");
        }

        // Step 4: Decode QR code using ZXing
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result = reader.decode(bitmap);

        // Step 5: Extract vehicle number (before '|')
        String fullText = result.getText();
        String[] parts = fullText.split("\\|");
        if (parts.length > 0) {
            return parts[0]; // Vehicle number
        } else {
            throw new IOException("Invalid QR content format");
        }
    }
}

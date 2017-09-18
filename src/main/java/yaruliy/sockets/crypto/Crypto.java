package yaruliy.sockets.crypto;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    public static String decrypt(String text) throws Exception {
        byte [] pld = Base64.getDecoder().decode(text);
        byte [] devAddr = getDevAddr(pld);
        byte [] frameCounter = getFrameCounter(pld);
        byte [] result = initializeResult(pld);
        byte [] Ai = new byte[16];
        byte [] Si;

        byte [] K = {
                (byte)0xD6, (byte) 0x83, (byte) 0xC4, (byte)0x47, (byte)0x76,
                (byte)0x05, (byte)0x71, (byte)0xC3, (byte)0xAC, (byte)0xE6, (byte)0x6D,
                (byte)0xA9, (byte)0x88, (byte)0xCC, (byte)0xEC, (byte)0x9F
        };


        byte [] mass = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        for(int i = 0; i < result.length; i += 16) {
            int blockSeqCnt = (i >> 4) + 1;

            computeAi(Ai, devAddr, frameCounter, blockSeqCnt);
            Si = encryptAES(Ai, K, mass);

            for(int j=0; j < 16 && i+j < result.length; j++) {
                result[i+j] ^= Si[j];
            }
        }

        return Base64.getEncoder().encodeToString(result);
    }


    public static String toText(String decryptedText) {
        byte [] data = Base64.getDecoder().decode(decryptedText);
        StringBuilder plain = new StringBuilder();

        for (byte aData : data) {
            plain.append((char) aData);
        }

        return plain.toString();
    }


    public static byte [] getDevAddr(byte [] payload) {
        byte [] devAddr = new byte[4];
        System.arraycopy(payload, 1, devAddr, 0, 4);
        return devAddr;
    }

    public static byte [] getFrameCounter(byte [] payload) {
        byte [] frameCounter = new byte[2];
        System.arraycopy(payload, 6, frameCounter, 0, 2);
        return frameCounter;
    }

    public static byte [] initializeResult(byte [] payload) {
        byte [] result = new byte[payload.length - 13];
        System.arraycopy(payload, 9, result, 0, result.length);
        return result;
    }

    public static void computeAi(byte [] a, byte [] devAddr, byte [] frameCounter, int blockSeqCnt) {
        a[0]  = 0x01;
        a[1]  = 0x00;
        a[2]  = 0x00;
        a[3]  = 0x00;
        a[4]  = 0x00;
        a[5]  = 0;
        a[6]  = devAddr[0];
        a[7]  = devAddr[1];
        a[8]  = devAddr[2];
        a[9]  = devAddr[3];
        a[10] = frameCounter[0];
        a[11] = frameCounter[1];
        a[12] = 0x00;
        a[13] = 0x00;
        a[14] = 0x00;
        a[15] = (byte)blockSeqCnt;
    }

    public static byte[] encryptAES(byte [] data, byte [] key, byte [] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", "SunJCE");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }
}
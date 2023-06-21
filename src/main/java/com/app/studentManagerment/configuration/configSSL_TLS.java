package com.app.studentManagerment.configuration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class configSSL_TLS {
    public static void main(String[] args) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, KeyManagementException {
        // Khai báo URI
        String uri = "https://www.googleapis.com/drive/v3/";

        // Tạo HttpsURLConnection
        URL url = new URL(uri);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();


        // Lấy danh sách chứng chỉ từ HttpsURLConnection
        conn.connect(); // Mở kết nối
        Certificate[] certs = conn.getServerCertificates();

        // Khởi tạo keystore
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);

        // Thêm chứng chỉ vào keystore
        for (
                Certificate cert : certs) {
            X509Certificate x509 = (X509Certificate) cert;
            String alias = x509.getSubjectX500Principal().getName();
            ks.setCertificateEntry(alias, cert);
        }

        // Khởi tạo TrustManagerFactory và SSLContext để sử dụng keystore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        SSLContext ctx = SSLContext.getInstance("TLS");


        ctx.init(null, tmf.getTrustManagers(), null);

        // Thiết lập SSLContext mặc định cho HttpsURLConnection
        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

    }
}

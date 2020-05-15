package com.base.module_http.ssl;

import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * 创建人： $(USER)
 * 创建时间：$(DATE)
 * 页面说明：
 * 功能性修改记录：
 */
public class Tls12SocketFactory extends SSLSocketFactory {
	private static final String[] TLS_SUPPORT_VERSION = {"TLSv1.1", "TLSv1.2"};
	
	final SSLSocketFactory delegate;
	
	private static X509TrustManager getDefaultTrustManager() {
		try {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
					TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init((KeyStore) null);
			TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
			if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
				throw new IllegalStateException("Unexpected default trust managers:"
						+ Arrays.toString(trustManagers));
			}
			return (X509TrustManager) trustManagers[0];
		} catch (GeneralSecurityException e) {
			throw new AssertionError(); // The system has no TLS. Just give up.
		}
	}
	
	public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
		if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
			Tls12SocketFactory socketFactory = null;
			try {
				
				SSLContext sslContext = SSLContext.getInstance("TLS");
				sslContext.init(null, null, null);
				SSLSocketFactory lFactory = sslContext.getSocketFactory();
				socketFactory = new Tls12SocketFactory(lFactory);
				client.sslSocketFactory(socketFactory, getDefaultTrustManager());
				
			} catch (Exception e) {
				
				try {
					SSLContext sc = SSLContext.getInstance("TLSv1.2");
					sc.init(null, null, null);
					client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));
					client.sslSocketFactory(socketFactory, getDefaultTrustManager());


//					client.connectionSpecs(specs);
				} catch (Exception exc) {
					
					Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
					
				}
			}
			
			return client;
		} else {
			return client;
		}
	}
	
	
	public Tls12SocketFactory(SSLSocketFactory base) {
		this.delegate = base;
	}
	
	@Override
	public String[] getDefaultCipherSuites() {
		return delegate.getDefaultCipherSuites();
	}
	
	@Override
	public String[] getSupportedCipherSuites() {
		return delegate.getSupportedCipherSuites();
	}
	
	@Override
	public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
		return patch(delegate.createSocket(s, host, port, autoClose));
	}
	
	@Override
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return patch(delegate.createSocket(host, port));
	}
	
	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
		return patch(delegate.createSocket(host, port, localHost, localPort));
	}
	
	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		return patch(delegate.createSocket(host, port));
	}
	
	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
		return patch(delegate.createSocket(address, port, localAddress, localPort));
	}
	
	private Socket patch(Socket s) {
		if (s instanceof SSLSocket) {
			((SSLSocket) s).setEnabledProtocols(TLS_SUPPORT_VERSION);
		}
		return s;
	}
	
}

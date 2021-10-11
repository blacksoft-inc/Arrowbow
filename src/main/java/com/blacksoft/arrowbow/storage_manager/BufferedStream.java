package com.blacksoft.arrowbow.storage_manager;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * Max Buffer size is 2GB
 */

public class BufferedStream {
    private final ArrayList<Byte> bytes;

    /**
     * empty Constructor
     */
    public BufferedStream() {
        bytes = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param b : a byte to be added while initializing the bytes list
     */
    public BufferedStream(byte b) {
        this();
        bytes.add(b);
    }

    /**
     * Constructor
     *
     * @param bytes : a bytes vector to be added in the list.
     */
    public BufferedStream(byte[] bytes) {
        this();
        add(bytes);
    }

    /**
     * Constructor
     *
     * @param bytes : a bytes list to be added in the list.
     */
    public BufferedStream(ArrayList<Byte> bytes) {
        this();
        add(bytes);
    }

    /**
     * Constructor
     *
     * @param inputStream: input stream from where u want to read bytes.
     */
    public BufferedStream(InputStream inputStream) {
        this();
        add(inputStream);
    }

    /**
     * Clear Buffer content
     */
    public void clear() {
        bytes.clear();
    }

    /**
     * @return the number of bytes contained in the buffer
     */
    public long getBytesCount() {
        return bytes.size();
    }

    /**
     * adding one byte to stream
     *
     * @param b: the byte to be added
     */
    public void add(byte b) {
        bytes.add(b);
    }

    /**
     * adding a vector of bytes to stream
     *
     * @param bytes: the bytes vector to be added
     */
    public void add(byte[] bytes) {
        if (bytes != null) {
            byte b;
            for (int i = 0; i < bytes.length; i++) {
                b = bytes[i];
                if (b != 0) this.bytes.add(b);
                else break;
            }
        }
    }

    /**
     * adding a list of bytes to stream.
     *
     * @param bytes: the bytes list to be added.
     */
    public void add(List<Byte> bytes) {
        if (bytes != null)
            this.bytes.addAll(bytes);
    }

    /**
     * adding a list of bytes to stream.
     *
     * @param inputStream: input stream from where u want to read bytes.
     */
    public void add(InputStream inputStream) {
        if (inputStream != null) {
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                byte[] buffer = new byte[4096]; /* reading by 4kb */
                while ((bufferedInputStream.read(buffer)) != -1) {
                    add(buffer);
                }
                inputStream.close();
            } catch (Exception e) {
            }
        }

    }

    /**
     * @param index: index of byte you want to get.
     * @return byte at index if not exists returns 0.
     */
    public byte getByte(int index) {
        if (index > -1 && index < bytes.size())
            return bytes.get(index);
        else return 0;
    }

    /**
     * @return a vector of bytes.
     */
    public byte[] getBytesArray() {
        byte[] bytes = new byte[this.bytes.size()];
        for (int i = 0; i < this.bytes.size(); i++) {
            bytes[i] = this.bytes.get(i);
        }
        return bytes;
    }

    /**
     * @return a vector of bytes.
     */
    public ArrayList<Byte> getBytesList() {
        return bytes;
    }


}
package com.kincony.server.util;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 文件分片器
 * @author JOEY
 *
 */
public class FileSegmentor {
	private RandomAccessFile file;
	// 总分片数
    private int totalFragments;
    // 分片大小
    private int fragmentSize;
    // 文件大小
    private long fileSize;
    
    /**
     * 创建一个文件分片器
     * 
     * @param path
     * @param fragmentSize
     */
    public FileSegmentor(String path, int fragmentSize) {
        try {
            this.fragmentSize = fragmentSize;
            file = new RandomAccessFile(path, "r");
            fileSize = file.length();
	        totalFragments = (int)(file.length() - 1) / fragmentSize + 1;
        } catch (IOException e) {
            totalFragments = 0;
        }        
    }
    
    /**
     * @return
     * 		true表示这个文件成功打开
     */
    public boolean isLoadSuccess() {
        return totalFragments > 0;
    }
    
    /**
     * 得到分片数据
     * 
     * @param index
     * 		分片位置
     * @return
     * 		数据字节数组
     */
    public byte[] getFragment(int index) {
        if(index >= totalFragments)
            return null;
        
        try {
            byte[] ret = null;
            if(index < totalFragments - 1)
                ret = new byte[fragmentSize];
            else
                ret = new byte[(int)file.length() % fragmentSize];
            
            file.seek(index * fragmentSize);
            file.readFully(ret);
            return ret;
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * 关闭打开的文件
     */
    public void close() {
        try {
            file.close();
        } catch (IOException e) {
        }
    }
    
    /**
     * @return Returns the totalFragments.
     */
    public int getTotalFragments() {
        return totalFragments;
    }

	public long getFileSize() {
		return fileSize;
	}

	public int getFragmentSize() {
		return fragmentSize;
	}
}

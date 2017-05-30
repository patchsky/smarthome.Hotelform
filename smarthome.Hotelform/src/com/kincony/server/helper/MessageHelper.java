package com.kincony.server.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.kincony.server.util.FileSegmentor;

public class MessageHelper {
	// 分片缓冲
	private Map<Integer, Object[]> fragmentCache;

	public MessageHelper() {
		fragmentCache = new HashMap<Integer, Object[]>();

	}

	/**
	 * 初始化文件分片
	 * @param messageId
	 * @param fileSegmentor
	 */
	public void initFragment(int messageId, FileSegmentor fileSegmentor){
		int totalFragments = fileSegmentor.getTotalFragments();
		Object[] fragments = fragmentCache.get(messageId);
		if (fragments == null || fragments.length != totalFragments) {
			fragments = new Object[totalFragments];
			fragmentCache.put(messageId, fragments);
		}
		
		for(int index=0; index<totalFragments; index++){
			fragments[index] = fileSegmentor.getFragment(index);
		}
	}
	
	/**
	 * 得到分片数据包
	 * @param messageId
	 * @param fragment
	 * @return
	 */
	public byte[] getFragment(int messageId, int index){
		if(!fragmentCache.containsKey(messageId))
            return null;
		
		Object[] fragments = fragmentCache.get(messageId);
		if(fragments.length <= index){
			 return null;
		}
		
		if(fragments.length == index - 1){ // 最后一个
			fragmentCache.remove(messageId);
		}
		return (byte[])fragments[index];
	}
}

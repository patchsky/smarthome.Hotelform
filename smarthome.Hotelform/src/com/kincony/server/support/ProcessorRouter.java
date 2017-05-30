package com.kincony.server.support;

import java.util.List;
import java.util.Vector;

import com.kincony.server.event.IPacketListener;
import com.kincony.server.event.PacketEvent;

public class ProcessorRouter {
	private List<IPacketListener> listeners, listenersBackup;
    private boolean listenerChanged;
    
    public ProcessorRouter(int capacity) {
        listeners =  new Vector<IPacketListener>();
        listenersBackup =  new Vector<IPacketListener>();
    }
    
    /**
     * 
     */
    public synchronized void setListenerChanged(boolean b){
    	listenerChanged = b;
    }
    
    /**
     * 装载包处理器
     * 
     * @param listener
     */
    public void installProcessor(IPacketListener listener) {
    	 if(!listeners.contains(listener)) {
    		 listeners.add(listener);
	    	setListenerChanged(true);    		
		}
    }
    
    /**
     * 移去一个监听器
     * @param listener
     */
    public void removeProcessor(IPacketListener listener) {
    	if(listeners.contains(listener)) {
    		listeners.remove(listener);
	    	setListenerChanged(true);    		
    	}
    }
    
    /**
     * 得到listener changed标志
     * 
     * @return
     * 		true表示有新的listener添加了进来
     */
    private synchronized boolean isListenerChanged() {
    	return listenerChanged;
    }
    
    /**
     * 检查监听器是否已经改变，如果是则更新监听器
     */
    private synchronized void checkListenerChange() {
    	if(isListenerChanged()) {
    		listenersBackup.clear();
    		listenersBackup.addAll(listeners);
    		setListenerChanged(false);
    	}
    }
    
    /**
     * 包到达时，调用此方法
     * 
     * @param e
     */
    public void packetArrived(PacketEvent e) {
    	checkListenerChange();
		int size = listenersBackup.size();
		for(int i = 0; i < size; i++){			
			listenersBackup.get(i).packetArrived(e);
		}
    }
}

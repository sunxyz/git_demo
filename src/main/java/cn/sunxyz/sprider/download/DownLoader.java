package cn.sunxyz.sprider.download;

import cn.sunxyz.sprider.Request;
import cn.sunxyz.sprider.processor.Page;

public interface DownLoader {
	
	Page dowloader(Request request);
	
	Page dowloader(String request);

}

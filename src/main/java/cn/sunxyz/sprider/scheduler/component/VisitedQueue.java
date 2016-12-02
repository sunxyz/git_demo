package cn.sunxyz.sprider.scheduler.component;

import java.io.Serializable;

import cn.sunxyz.sprider.Request;

public interface VisitedQueue extends Serializable {

	void push(Request request);

	boolean hasExisted(Request request);

	void clear();

	int size();
}

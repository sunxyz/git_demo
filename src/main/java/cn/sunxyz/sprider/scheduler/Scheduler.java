package cn.sunxyz.sprider.scheduler;

import java.io.Serializable;

import cn.sunxyz.sprider.Request;

public interface Scheduler extends Serializable {

	void push(Request request);

	Request pop();

	int size();

}

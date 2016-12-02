package cn.sunxyz.sprider.scheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cn.sunxyz.sprider.Request;

public class QueueScheduler implements Scheduler {

	private static final long serialVersionUID = 6332664778728241726L;

	private BlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();

	@Override
	public void push(Request request) {
		queue.add(request);
	}

	@Override
	public Request pop() {
		return queue.poll();
	}

	@Override
	public int size() {
		return queue.size();
	}

}

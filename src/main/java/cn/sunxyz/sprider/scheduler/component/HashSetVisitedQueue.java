package cn.sunxyz.sprider.scheduler.component;

import java.util.Set;

import cn.sunxyz.sprider.Request;
import cn.sunxyz.sprider.utils.ConcurrentHashSet;

public class HashSetVisitedQueue implements VisitedQueue {

	private static final long serialVersionUID = -7348939406939292109L;

	private Set<String> urls = new ConcurrentHashSet<>();

	@Override
	public void push(Request request) {
		urls.add(getUrl(request));
	}

	@Override
	public boolean hasExisted(Request request) {
		return urls.add(getUrl(request));
	}

	protected String getUrl(Request request) {
		return request.getUrl();
	}

	@Override
	public void clear() {
		urls.clear();
	}

	@Override
	public int size() {
		return urls.size();
	}
}

package com.shape100.gym.config;

import java.util.LinkedList;
import java.util.Queue;

import com.shape100.gym.model.BlogData;

public class QueueData {
	private static Queue<BlogData> queue;

	private QueueData() {

	}

	public static synchronized Queue<BlogData> getinstence() {
		if (queue == null) {
			queue = new LinkedList<BlogData>();
		}
		return queue;
	}
}

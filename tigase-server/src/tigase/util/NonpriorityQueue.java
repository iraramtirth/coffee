/*
 * Tigase Jabber/XMPP Server
 * Copyright (C) 2004-2012 "Artur Hefczyc" <artur.hefczyc@tigase.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. Look for COPYING file in the top folder.
 * If not, see http://www.gnu.org/licenses/.
 *
 * $Rev: 2996 $
 * Last modified by $Author: wojtek $
 * $Date: 2012-08-21 06:29:57 +0800 (Tue, 21 Aug 2012) $
 */

package tigase.util;

//~--- JDK imports ------------------------------------------------------------

import java.util.concurrent.LinkedBlockingQueue;

//~--- classes ----------------------------------------------------------------

/**
 * Created: Feb 9, 2010 11:32:30 AM
 *
 * @param <E>
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class NonpriorityQueue<E> extends PriorityQueueAbstract<E> {
	private LinkedBlockingQueue<E> queue = null;

	//~--- constructors ---------------------------------------------------------

	/**
	 * Constructs ...
	 *
	 */
	public NonpriorityQueue() {}

	protected NonpriorityQueue(int maxSize) {
		init(0, maxSize);
	}

	//~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param maxPriority
	 * @param maxSize
	 */
	@Override
	public final void init(int maxPriority, int maxSize) {
		queue = new LinkedBlockingQueue<E>(maxSize);
	}

	/**
	 * Method description
	 *
	 *
	 * @param element
	 * @param priority
	 *
	 * @return
	 */
	@Override
	public boolean offer(E element, int priority) {
		return queue.offer(element);
	}

	/**
	 * Method description
	 *
	 *
	 * @param element
	 * @param priority
	 *
	 * @throws InterruptedException
	 */
	@Override
	public void put(E element, int priority) throws InterruptedException {
		queue.put(element);
	}

	//~--- set methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param maxSize
	 */
	@Override
	public void setMaxSize(int maxSize) {

//  TODO:
//  The code below causes a dead-lock as the take() method waits on the
//  old queue without any chance to be waken up. Up to now I haven't
//   found a way to awake the thread waiting on the take() method.
//     LinkedBlockingQueue<E> oldQueue = queue;
//     int newSize = Math.max(oldQueue.size(), maxSize);
//
//     queue = new LinkedBlockingQueue<E>(newSize);
//     oldQueue.drainTo(queue);
	}

	//~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	@Override
	public int[] size() {
		int[] result = new int[1];

		result[0] = queue.size();

		return result;
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 *
	 * @throws InterruptedException
	 */
	@Override
	public E take() throws InterruptedException {
		return queue.take();
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	@Override
	public int totalSize() {
		return queue.size();
	}
}


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com

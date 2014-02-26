/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom.algorithm;

import java.util.*;
import org.cake.game.geom.Line2;

/**
 *
 * @author Aaron
 */
public class ShamosHoey2 {

    public static boolean doShit(List<Line2> lines) {

        if (lines.size() < 2)
            return false;

        Queue<Event> eventQueue = new PriorityQueue<>(lines.size() * 2);
        for (Line2 line: lines) {
            int order = getOrder(line);
            if (order == 1) {
                eventQueue.add(new Event(line, line.x1, line.y1, true));
                eventQueue.add(new Event(line, line.x2, line.y2, false));
            } else if (order == -1) {
                eventQueue.add(new Event(line, line.x1, line.y1, false));
                eventQueue.add(new Event(line, line.x2, line.y2, true));
            } else {
                // uhh hurp duh derp its just a point...
            }
        }

        LineComparator lc = new LineComparator();
        TreeSet<Line2> sl = new TreeSet<>(lc);
        while (!eventQueue.isEmpty()) {
            Event e = eventQueue.poll();
            if (e.isLeft) {
                lc.linePos = e.x;
                Line2 above = sl.higher(e.line);
                if (above != null && above.intersects(e.line, false))
                    return true;
                Line2 below = sl.lower(e.line);
                if (below != null && below.intersects(e.line, false))
                    return true;
                sl.add(e.line);

            } else {
                lc.linePos = e.x;
                Line2 above = sl.higher(e.line);
                Line2 below = sl.lower(e.line);
                sl.remove(e.line);
                if (above != null && below != null && above.intersects(below, false))
                    return true;
            }
        }

        return false;

    }

    private static int getOrder(Line2 line) {
        return line.x1 < line.x2 ? 1 :
                (line.x1 > line.x2 ? -1 :
                (line.y1 < line.y2 ? 1 :
                (line.y1 > line.y2 ? -1 : 0)));
    }

    static class LineComparator implements Comparator<Line2> {

        public float linePos;

        @Override
        public int compare(Line2 l1, Line2 l2) {

            float m1 = l1.slope();
            float b1 = l1.y1 - m1 * l1.x1;
            float m2 = l2.slope();
            float b2 = l2.y1 - m2 * l2.x1;

            if (l1 == l2 || l1.equals(l2))
                return 0;

            float y1 = m1 * linePos + b1;
            float y2 = m2 * linePos + b2;

            if (y1 != y2)
                return y1 < y2 ? -1 : 1;

            if (m1 != m2)
                return m1 < m2 ? -1 : 1;

            if (l1.x1 != l2.x1)
                return l1.x1 < l2.x1 ? -1 : 1;

            return l1.x2 < l2.x2 ? -1 : 1;

        }
    }

    private static class Event implements Comparable<Event> {

        public boolean isLeft;
        public float x, y;
        public Line2 line;

        public Event(Line2 line, float x, float y, boolean isLeft) {
            this.isLeft = isLeft;
            this.x = x;
            this.y = y;
            this.line = line;
        }

        @Override
        public int compareTo(Event other) {
            return x < other.x ? -1 :
                    (x > other.x ? 1 :
                    (y < other.y ? -1 :
                    (y > other.y ? 1 : 0)));
        }

    }

}

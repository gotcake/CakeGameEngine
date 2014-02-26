/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.geom.algorithm;

import java.util.*;
import org.cake.game.geom.Line2;
import org.cake.game.geom.Vector2;
import org.cake.game.geom.iContour;

/**
 *
 * @author Aaron Cake
 */
public class ShamosHoey {

    public static boolean checkIntersection(Collection<Line2> lines) {
        List<AlgEvent> events = new ArrayList<>(lines.size() * 2);
        for (Line2 li : lines) {
            if (li.x1 < li.x2) {
                events.add(new AlgEvent(li, true));
                events.add(new AlgEvent(li, false));
            } else if (li.x1 > li.x2) {
                Line2 l = li.reverse();
                events.add(new AlgEvent(l, true));
                events.add(new AlgEvent(l, false));
            } else {
                if (li.y1 < li.y2) {
                    events.add(new AlgEvent(li, true));
                    events.add(new AlgEvent(li, false));
                } else if (li.y1 > li.y2) {
                    Line2 l = li.reverse();
                    events.add(new AlgEvent(l, true));
                    events.add(new AlgEvent(l, false));
                } else {
                    return true;
                }
            }
        }
        Collections.sort(events, new AlgEvtComparator());
        TreeSet<Line2> sl = new TreeSet<>(new LineComparator());
        for (AlgEvent e : events) {
            if (e.isStart) {
                Line2 nl = e.line;
                Line2 above = sl.higher(nl);
                if (above != null) {
                    if (above.intersects(nl)) {
                        return true;
                    }
                }
                Line2 below = sl.lower(nl);
                if (below != null) {
                    if (below.intersects(nl)) {
                        return true;
                    }
                }
                sl.add(nl);
            } else {
                Line2 nl = e.line;
                Line2 above = sl.higher(nl);
                Line2 below = sl.lower(nl);
                sl.remove(nl);
                if (above != null && below != null) {
                    if (above.intersects(below)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkIntersection(List<Vector2> contour) {
        List<Line2> lines = new ArrayList<>();
        int size = contour.size();
        for (int i=0, j = size - 1; i<size; j = i++) {
            lines.add(new Line2(contour.get(j), contour.get(i)));
        }
        return checkIntersection(lines);
    }


    /**
    * Moves first point of the line by 0.0000001 of it's length.
    * @return
    */
    static Line2 tweakLine(Line2 l) {
        return new Line2(
                l.x1 + 0.0000001f * (l.x2 - l.x1),
                l.y1 + 0.0000001f * (l.y2 - l.y1),
                l.x2 - 0.0000001f * (l.x2 - l.x1),
                l.y2 - 0.0000001f * (l.y2 - l.y1)
             );
    }

    static Line2 tweakLine(Vector2 p1, Vector2 p2) {
        return new Line2(
                p1.x + 0.0000001f * (p2.x - p1.x),
                p1.y + 0.0000001f * (p2.y - p1.y),
                p2.x - 0.0000001f * (p2.x - p1.x),
                p2.y - 0.0000001f * (p2.y - p1.y)
             );
    }

    static class AlgEvent {

        public Line2 line;
        public boolean isStart;

        AlgEvent(Line2 l, boolean isStart) {
            line = l;
            this.isStart = isStart;
        }

        Vector2 getPoint() {
            return (isStart) ? line.getP1() : line.getP2();
        }

        double getX() {
            return (isStart) ? line.x1 : line.x2;
        }

        double getY() {
            return (isStart) ? line.y1 : line.y2;
        }

        @Override
        public String toString() {
            return "start =  " + isStart + ", point = " + this.getPoint() + ", line = " + line.getP1() + " : " + line.getP2();
        }

    }

    static class AlgEvtComparator implements Comparator<AlgEvent> {

        @Override
        public int compare(AlgEvent o1, AlgEvent o2) {
            if (o1.getX() < o2.getX()) {
                return -1;
            } else if (o1.getX() > o2.getX()) {
                return 1;
            } else {
                if (o1.getY() < o2.getY()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }

    }

    /**
    * Class to compare lines, to ensure above-below order.
    */
    static class LineComparator implements Comparator<Line2> {

        @Override
        public int compare(Line2 o1, Line2 o2) {
             if (o1.y1 < o2.y1) {
                return -1;
            } else if (o1.y1 > o2.y2) {
                return 1;
            } else {
                if (o1.y2 < o2.y2) {
                    return -1;
                } else if (o1.y2 > o2.y2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }
}

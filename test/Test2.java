
import org.cake.game.geom.Line2;
import org.cake.game.geom.Vector2;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aaron
 */
public class Test2 {
    public static void main(String[] args) {
        
        Line2 line = new Line2(100, 100, 300, 300);
        
        Vector2 pt = new Vector2(100, 300);
        Vector2 v = new Vector2(2, -1);
        Vector2 v2 = new Vector2(-1, 1);
        Vector2 v3 = new Vector2(1, 1);
        
        System.out.println("1: " + rayIntersectsLine(line, pt, v));
        System.out.println("2: " + rayIntersectsLine(line, pt, v2));
        System.out.println("3: " + rayIntersectsLine(line, pt, v3));
        
    }
    static boolean rayIntersectsLine(Line2 l, Vector2 p, Vector2 v) {
	/*	float x4 = p.x + v.x;
		float y4 = p.y + v.y;
		float denom = (y4 - p.y) * (l.x2 - l.x1) - (x4 - p.y) * (l.y2 - l.y1);
		if (denom == 0) // paralell
			return false;
		// position of intersection relative to the line
        float numa = (x4 - p.y) * (l.y1 - p.y) - (y4 - p.y) * (l.x1 - p.y);
		// position of intersection relative to the ray
        float numb = (l.x2 - l.x1) * (l.y1 - p.y) - (l.y2 - l.y1) * (l.x1 - p.y);
        //float numb = (l.x2 - l.x1) * (l.y1 - p.y) - (l.y2 - l.y1) * (l.x1 - p.y);
        System.out.printf("numa: %s, numb: %s \n", numa, numb);
	return numb < 0;*/
         Vector2 p2 = v.product(100000).add(p);
         return isLeft(l.getP1(), l.getP2(), p) != isLeft(l.getP1(), l.getP2(), p2);
}
    
    static boolean isLeft(Vector2 a, Vector2 b, Vector2 c) {
        return ((b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x)) > 0;
   }
}

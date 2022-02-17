// Nested Json
// Nesting goes from highest level (top)
// to lowest level (bottom of file)
package org.echowear.rimcatbeta.data_log;


public class JsonCoord {
    private String ID;
    private fragment fragm;
    // date is just mm-dd-yy  -> no distinct time, that is nested further down
//    private String Date;
        public JsonCoord(String ID, fragment fragm) {
        super();
        this.ID = ID;
        this.fragm = fragm;
        // getters and setters
    }


}

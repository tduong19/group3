import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBDemo {

    private static boolean debug = true;

    /**
     * Create a connection to the database.
     * 
     * @@@ Probably want a class for encapsulating all database activities.
     *
     * @return a JDBC Connection
     */
    public static Connection dbConnect() {
        Connection conn = null;
        String protocol = "jdbc:sqlite:";
        //Users/Carlos/workspace/DMDemo/propertyDB
        //User/
        String dbName = ; // the full path of the database
        String connString = protocol + dbName;

        try {
            // @@@ Either of these will work
            //Class.forName("org.sqlite.JDBC"); // throws ClassNotFoundException
            DriverManager.registerDriver(new org.sqlite.JDBC()); // throws SQLException
        }
        catch (SQLException ex) {
            System.err.println("dbConnect: Received ClassNotFoundException when trying "
                    + "to start SQLite: " + ex.getMessage());
            System.exit(1);
        }
//        catch (ClassNotFoundException ex) {
//            System.err.println("dbConnect: Received ClassNotFoundException when trying "
//                    + "to start SQLite: " + ex.getMessage());
//            System.exit(1);
//        }
        
        try {
            conn = DriverManager.getConnection(connString);
            DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
            if (debug) {
                System.out.println("dbConnect: Connected to database " + connString);
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }
        }
        catch (SQLException ex) {
            System.err.println("Received SQLException when trying to open db: "
                    + connString + " " + ex.getMessage());
            System.err.println("Connection string: " + connString);
            System.exit(1);
        }
        return conn;
    }

    /**
     * Main activities: 
     * 1. Drop the table if it exists
     * 2. create an empty table. 
     * 3. Add items into the table. 
     * 4. Print the table values. 
     * This is NOT a complete test suite.
     *
     * @param args Ignored, but required by Java
     */
    public static void main(String args[]) {
        Connection db = dbConnect();
        PropertyTable table = new PropertyTable(db);

        if (table.checkTableExists()) {
            if (debug) System.out.println("Table property exists.");
            table.dropTable();
        } else {
            if (debug) System.out.println("Table does not exist.  About to create it.");
        }
        table.createTable();
        Property p1 = new Property("A", CityCode.ABQ, "123 4th NW", "87107", 1);
        if (debug) System.out.println(table.insert(p1) ? "Inserted" : "Insert failed");
        Property p2 = new Property("S", CityCode.SAF, "123 Agua Fria", "87108", 1);
        if (debug) System.out.println(table.insert(p2) ? "Inserted" : "Insert failed");
        Property p3 = new Property("V", CityCode.ROW, "123 Alien Way", "87109", 2);
        if (debug) System.out.println(table.insert(p3) ? "Inserted" : "Insert failed");
        
        System.out.println("Table contents:");
        for (Property p : table.getAll()) {
            System.out.println(p.toString());
        }
        
        System.out.println("Properties in ABQ:");
        for (Property p : table.getCity(CityCode.ABQ)) {
         //   System.out.println(p.getAddress().toString());
            System.out.println(p.toString());
        }
        System.out.println("Properties in Apartment Rental");
        for(Property p : table.getType("S")){
        System.out.println(p.toString());
        }
        System.out.println("Properties with this # of bedroom");
        for(Property p : table.getBedRoomCount(1)){
            System.out.println(p.toString());
        }
        
    }
}
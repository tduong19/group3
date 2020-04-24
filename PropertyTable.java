import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class PropertyTable {
    private static boolean debug = true;
    private Connection db; // cached JDBC Connection
    private final String tableName = "property"; // Database table name
    private final String createsql; // the SQL statement needed to create the table

    /**
     * Constructor for an empty property table.  The table is tied to the 
     * underlying database.
     * 
     * @@@ Note caching of Connection
     * @@@ Note SQL creation string is based on Property
     * 
     * @param db a JDBC Connection to the database
     */
    PropertyTable(Connection db) {
        if (db == null) {
            throw new IllegalArgumentException("PropertyTable constructor: db connection is null");
        }
        this.db = db;
        this.createsql = "create table property" + Property.getSQLcreate() + ";";
    }

    /**
     * Insert a property into the table.
     * 
     * @param p the property to insert
     * @return a boolean indicating success or failure
     */
    public boolean insert(Property p) {
        if (debug) System.out.println("About to insert property: " + p.toString());

        PreparedStatement ps = null;
        String sql = p.getInsertPSString(tableName);
        int nInserted = 0;
        try {
            ps = db.prepareStatement(sql);
            p.bindvars(ps); // keep all SQL related to the property in Property
            nInserted = ps.executeUpdate();
        }
        catch (SQLException ex) {
            System.err.printf("insert: Received SQLException when trying to create or execute statement: %s",
                    ex.getMessage());
            System.err.println("SQL: " + sql);
            System.exit(1);
        }

        return nInserted == 1;
    }

    /**
     * Return the complete list of properties in the table.
     * 
     * @return an ArrayaList of Property
     */
    public ArrayList<Property> getAll() {
        if (debug) System.out.println("About to getAll");

        return selectSome(Property.getSelectString(tableName));
    }
    
    /**
     * Return a list of properties in a given city.
     * 
     * @@@ This code needs to have common code with getAll extracted to a method
     * 
     * @param city The city to select property from
     * @return an ArrayaList of Property
     */
    public ArrayList<Property> getCity(CityCode city) {
        if (debug) System.out.println("About to getCity for " + city.getFullName());
        return selectSome(Property.getSelectString(tableName, city));
    }
    
    public ArrayList<Property> getType(String type) {
        if (debug) System.out.println("About to get type for " + type);
        return selectSome(Property.getSelectStringForType(tableName, type));
    }
    public ArrayList<Property> getBedRoomCount(Integer type) {
        if (debug) System.out.println("About to get bed room count for " + type);
        return selectSome(Property.getSelectStringForBedRoomCount(tableName, type));
    }
    
//    public <T> ArrayList<Property> getParameter(T item){
//        if (debug) System.out.println("About to " item.toString() + " for " + item);
//        return selectSome(Property.getSelectString(tableName, item));
//    }
    
    private ArrayList<Property> selectSome(String sql) {
        ArrayList<Property> properties = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet result = null;
        
        try {
            ps = db.prepareStatement(sql);
            result = ps.executeQuery();
            while (result.next()) {
                properties.add(new Property(result));
            }
            result.close();
        }
        catch (SQLException ex) {
            System.err.printf("getAll: Received SQLException when trying to create or execute statement: %s",
                    ex.getMessage());
            System.err.println("SQL: " + sql);
            System.exit(1);
        }
        return properties;
    }

    /**
     * Drop the table and all of its contents.  Use this method with caution!
     */
    public void dropTable() {
        PreparedStatement ps = null;
        String sql = "DROP TABLE property;";
        try {
            ps = db.prepareStatement(sql);
            ps.execute();
        }
        catch (SQLException ex) {
            System.err.println("dropTable: Received SQLException when trying to create or execute statement: "
                    + ex.getMessage());
            System.err.println("SQL: " + sql);
            System.exit(1);
        }
        if (debug) System.out.println("Dropped table property");
    }

    /**
     * Create an empty table.
     */
    public void createTable() {
        PreparedStatement ps = null;

        try {
            ps = db.prepareStatement(createsql);
            ps.execute();
        }
        catch (SQLException ex) {
            System.err.println("createTable: Received SQLException when trying to create or execute statement: "
                    + ex.getMessage());
            System.err.println("SQL: " + createsql);
            System.exit(1);
        }
        if (debug) System.out.println("Created table property");
    }

    /**
     * See if the table exists or not.  Commented out code here might be useful
     * in other contexts.
     * 
     * @return a boolean indicating if the table exists
     */
    public boolean checkTableExists() {
        boolean answer = false;
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"
                + tableName + "';";
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            ps = db.prepareStatement(sql);
            if (ps.execute()) {
//                System.out.println("checkTableExists: execute returned true");
                result = ps.getResultSet();
//                ResultSetMetaData rsmd = result.getMetaData();
//                System.out.println("Col count: " + rsmd.getColumnCount());
//                for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
//                    System.out.print("Column: " + i);
//                    System.out.print("; col label: " + rsmd.getColumnLabel(i));
//                    System.out.println("; col name: " + rsmd.getColumnName(i));
//                }
                if (!result.isClosed()) {
                    result.next();
                    if (result.getRow() >= 0) {
                        //System.out.println("Cursor on row " + result.getRow());
                        answer = result.getString(1).equals(tableName);
                    }
                    result.close();
                } else {
                    System.out.println("result set is closed.");
                }
//            } else {
//                System.out.println("checkTableExists: execute returned false");
            }
        }
        catch (SQLException ex) {
            System.err.println("checkTableExists: Received SQLException "
                    + "when trying to create or execute: " + sql);
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        return answer;
    }
}

/*
Reference code.  Might be useful in other areas.  Code is also old, so use with caution.

    public static PreparedStatement createPStatement(Connection c, String sql) {
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(sql);
        }
        catch (SQLException ex) {
            System.err.println("Received SQLException when trying to create statement: "
                    + sql + " " + ex.getMessage());
            System.exit(1);
        }
        System.out.println("Created prepared statement from " + sql);
        System.out.println(ps.toString());
        return ps;
    }

    public static ResultSet executePSQuery(PreparedStatement ps) {
        ResultSet result = null;
        try {
            result = ps.executeQuery();
        }
        catch (SQLException ex) {
            System.err.println("Received SQLException when trying to "
                    + "execute prepared statement: " + ex.getMessage());
            System.exit(1);
        }
        return result;
    }

*/
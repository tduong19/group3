import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Property {

    private Integer id;
    private String type;
    private Integer bedRoomCount;
    private Integer bathroomCount;
    private Integer garageCount;
    private Integer squareFootageProperty;
    private Integer squareFootageFrontYard;
    
    private CityCode cityCode; // should be enumerated class
    private static final Integer cityCodeLength = 3;
    private String streetAddress;
    private static final Integer minAddrLength = 5;
    private static final Integer maxAddrLength = 40;
    private final String state = "NM"; // only have property in this state
    private String zipCode;
    private static final Integer zipCodeLength = 5;
    private final static String SQLcreate = "(propertyid INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "type CHAR(1), "
            + "citycode CHAR(" + CityCode.getMaxLength() + "), "
            + "addr VARCHAR(" + maxAddrLength.toString() + "),"
            + "zipcode char(10), "
            + "bedroomcount"
            + ")";

    /**
     * Constructor from data provided by a programmer.  Validate data before 
     * storing it.
     * 
     * @param type Rental type
     * @param cityCode In which city is the property
     * @param streetAddress Where in the city is it
     * @param zipCode The zip code for the property
     */
    Property(String type, CityCode cityCode, String streetAddress, String zipCode, Integer bedRoomCount ) {
        validateType(type);
        validateAddr(streetAddress);
        validateZip(zipCode);
        validateBedRoomCount(bedRoomCount);
        this.type = type;
        this.cityCode = cityCode;
        this.streetAddress = streetAddress;
        this.zipCode = zipCode;
        this.bedRoomCount = bedRoomCount;
    }
    
    /**
     * Constructor based on data from the database.  Database data is not 
     * magically correct, so we validate it first.
     * 
     * @@@ Input validation from DB
     * 
     * @param result a ResultSet from a SQL SELECT statement
     * @throws SQLException because we get items from the result
     */
    Property(ResultSet result) throws SQLException {
        String dbType, dbAddr, dbCity, dbZip;
        Integer dbID, dbBedRoomCount;
        // id, type, citycode, addr, zipcode
        dbID = result.getInt("propertyid");
        dbType = result.getString("type");
        dbCity = result.getString("citycode");
        dbAddr = result.getString("addr");
        dbZip = result.getString("zipcode");
        dbBedRoomCount = result.getInt("bedroomcount");
        
        validateType(dbType);
        validateAddr(dbAddr);
        validateZip(dbZip);
        validateCity(dbCity);
        validateID(dbID);
        validateBedRoomCount(dbBedRoomCount);
        this.id = dbID;
        this.type = dbType;
        this.cityCode = CityCode.valueOf(dbCity);
        this.streetAddress = dbAddr;
        this.zipCode = dbZip;
        this.bedRoomCount = dbBedRoomCount;
    }

    /**
     * Verify that the type is a legal value.
     * @param type String representing the property type
     */
    private void validateType(String type) {
        if (type == null) {
            throw new IllegalArgumentException(String.format("Type must not be null"));
        }

        if (type.length() != 1) {
            throw new IllegalArgumentException(String.format("Type '%s' is %d characters long; length is 1",
                    type, type.length()));
        }

        String validTypes = "A, S, or V";
        switch (type.charAt(0)) {
            case 'A':
            case 'S':
            case 'V':
                break; // OK
            default:
                throw new IllegalArgumentException(String.format("Illegal property type '%s'; valid types are: %s",
                        type, validTypes));
        }
    }

    /**
     * Validate an address
     * 
     * @param streetAddress the address to validate 
     */
    private void validateAddr(String streetAddress) {
        if (streetAddress == null) {
            throw new IllegalArgumentException(String.format("Street address must not be null"));
        }

        if (streetAddress.length() < minAddrLength) {
            throw new IllegalArgumentException(String.format("Street address '%s' is %d characters long; min length is %d",
                    streetAddress, streetAddress.length(), minAddrLength));
        }
        if (streetAddress.length() > maxAddrLength) {
            throw new IllegalArgumentException(String.format("Street address '%s' is %d characters long; max length is %d",
                    streetAddress, streetAddress.length(), maxAddrLength));
        }
        // should do other checking here
    }

    /**
     * Validate the zip code.
     * 
     * @param zipCode the zip code to validate
     */
    private void validateZip(String zipCode) {
        if (zipCode == null) {
            throw new IllegalArgumentException(String.format("zip code must not be null"));
        }
        if (zipCode.length() != zipCodeLength) {
            throw new IllegalArgumentException(String.format("Illegal zip code '%s' (length %d); valid codes are %d chars",
                    zipCode, zipCode.length(), zipCodeLength));
        }
        if (!zipCode.matches("^[0-9]*$")) {
            throw new IllegalArgumentException(String.format("Illegal zip code '%s'; valid codes only contain digits", zipCode));
        }
        // Should validate other parts.  For example, is zip code really in NM?
    }
    
    /**
     * Validate a string city code as being valid.  This is just a wrapper 
     * around CityCode's validation routine.  It is here for consistency.
     * 
     * @param city String to verify matches a city code
     */
    private void validateCity(String city) {
        CityCode.validateCode(city);
    }
    
    /**
     * Validate an ID integer.  Should be > 0
     * @param ID the ID to validate
     */
    private void validateID(Integer ID) {
        if (ID <= 0) {
            throw new IllegalArgumentException(String.format("ID %d is invalid; must be > 0", ID));
        }
    }
    private void validateBedRoomCount(Integer bedRoomCount){
        if (bedRoomCount <= 0){
            throw new IllegalArgumentException(String.format("BedRoomCount %d is invalid; must be > 0", bedRoomCount));
        }
    }

    public String getType() {
        return type;
    }
    public Integer getBedRoomCount(){
        return bedRoomCount;
    }

    public CityCode getCityCode() {
        return cityCode;
    }

    public String getAddress() {
        return streetAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public static String getSQLcreate() {
        return SQLcreate;
    }

    /**
     * Return a String version of the property.  Notable mainly because we 
     * have to handle if we have not got a valid ID.
     * 
     * @return String version of the property
     */
    public String toString() {
        if (id == null) id = -1;
        return String.format("%s%s%03d; %s, %s %s %s %d", type, cityCode.toString(),
                id, streetAddress,
                cityCode.getFullName(), state, zipCode, bedRoomCount);
    }

    /**
     * Return the SQL needed to insert a property into the database.
     * @param table The name of the table into which we should insert.
     * @return the SQL needed for an insert.
     */
    public String getInsertPSString(String table) {
        return "INSERT INTO " + table + "(type, citycode, addr, zipcode, bedRoomCount)"
                + "values (?,?,?,?,?);";
    }

    /**
     * Bind variables to the prepared statement.  This method <b>must</b> be 
     * consistent with the value returned by getInsertPSString().
     * 
     * @param ps The prepared statement into which we will insert values.
     * @throws SQLException when things go wrong with the prepared statement
     */
    public void bindvars(PreparedStatement ps) throws SQLException {
        ps.setString(1, type);
        ps.setString(2, cityCode.toString());
        ps.setString(3, streetAddress);
        ps.setString(4, zipCode);
        ps.setString(5, bedRoomCount.toString());
    }
    
    /**
     * Return the string needed to select a property from the database.  This 
     * method <b>must</b> be consistent with the constructor that takes a 
     * ResultSet.
     * 
     * @param table Name of the table where the data is stored
     * @return the SQL for selecting one or more table entries.
     */
    public static String getSelectString(String table) {
        return String.format("SELECT propertyid, type, citycode, addr, zipcode, bedroomcount from %s;", table);
    }
    public static String getSelectString(String table, CityCode city) {
        return String.format("SELECT propertyid, type, citycode, addr, zipcode, bedroomcount from %s where citycode = '%s';", table, city.toString());
    }
    public static String getSelectStringForType(String table, String type){
        return String.format("SELECT propertyid, type, citycode, addr, zipcode, bedroomcount from %s where type = '%s';", table, type);
    }
    public static String getSelectStringForBedRoomCount(String table, Integer bedRoomCount){
        System.out.println(bedRoomCount.toString());
        return String.format("SELECT propertyid, type, citycode, addr, zipcode, bedroomcount from %s where bedRoomCount = '%d';", table, bedRoomCount);
    }
    
}
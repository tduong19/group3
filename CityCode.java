public enum CityCode {
    ABQ("Albuquerque"), SAF("Santa Fe"), ROW("Roswell");
    private String fullName;
    private final static Integer minLength = 3;
    private final static Integer maxLength = 3;
    
    CityCode(String name) {
        fullName = name;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public static Integer getMaxLength() {
        return maxLength;
    }
    
    public static Integer getMinLength() {
        return minLength;
    }
    
    /**
     * Verify that a string is a valid CityCode
     * @param code the string to check.
     */
    public static void validateCode(String code) {
        String valid = "";
        for (CityCode c: CityCode.values()) {
            if (code.equals(c.toString())) {
                return;
            }
            valid += c.toString();
            valid += ", ";
        }
        
        // If we are here, the string is not valid.
        // Need to remove final ", "
        valid = valid.substring(0, valid.length()-3); // -1 for 0 index, -2 for ", "
        
        throw new IllegalArgumentException(String.format("Invalid city code '%s'.  Valid codes are %s", code, valid));
    }
}
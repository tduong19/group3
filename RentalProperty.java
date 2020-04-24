
public abstract class RentalProperty implements Payment{
    private String id;
    private int bedroomCount;
    public double increase;
    
    
    public RentalProperty (String id, int bedroomCount){
        if(validate(id, bedroomCount) == true){
        this.id = id;
        this.bedroomCount = bedroomCount;
        }
    }
    

    public boolean validate (String id, int bedroomCount){
        int count =0;
        for( int i = 0; i < id.length(); i ++){
            count ++; 
            if ( count > 7){
                throw new RuntimeException("The Id needs to be 7"
                        + " characters not more");
            }
            }
        if( count < id.length()){
            throw new RuntimeException("The id needs to be 7"
                    + "characters not less.");
        }
        if (bedroomCount <0 || bedroomCount > 3){
            return false;
        }
        return true;

        }
    @Override
    public String toString(){        
        return String.format("%s           %d          ",getId(), getBedroomCount());
    }
    public String getId(){
        return id;
    }
    public int getBedroomCount(){
        return bedroomCount;
    }
    public double getMoneyDue(){
        return moneyDue();
    }
    public void setId(String id){
        this.id = id;
    }
    public void setBedRoomCount(int bedroomCount){
        this.bedroomCount = bedroomCount;
    }
    }


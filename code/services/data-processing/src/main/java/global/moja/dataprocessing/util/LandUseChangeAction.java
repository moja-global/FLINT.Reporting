package global.moja.dataprocessing.util;

public enum LandUseChangeAction {
    
    CONVERSION("Converted To"),
    REMAINING("Remaining");
    
    private LandUseChangeAction(String description) {
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }
    
    private String description;
}

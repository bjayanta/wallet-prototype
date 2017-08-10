package helpers;

/**
 * Created by Jayanta on 8/10/2017.
 */

public class PairCls {

    private String id;
    private String name;

    public PairCls(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //to display object as a string in spinner
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PairCls){
            PairCls c = (PairCls)obj;
            if(c.getName().equals(name) && c.getId()==id ) return true;
        }

        return false;
    }
}

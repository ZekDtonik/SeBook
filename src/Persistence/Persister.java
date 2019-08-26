package Persistence;

import java.io.Serializable;
public abstract class Persister<T> implements Serializable {

    public abstract Integer getId() ;
    public abstract void setId(Integer id);

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Persister<?>) {
            return true;
        }
        else if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Persister<T> other = (Persister<T>) obj;

        if (this.getId() != other.getId()) {
            return false;
        } else if (!getId().equals(other.getId()))
            return false;

        return true;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());

        return result;
    }
}

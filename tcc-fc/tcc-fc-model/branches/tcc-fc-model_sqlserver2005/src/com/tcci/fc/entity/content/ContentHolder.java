package com.tcci.fc.entity.content;

//import com.tcci.fc.entity.content.ApplicationData;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.essential.TcDomain;
import java.util.Collection;

/**
 *
 * @author Wayne_Hu
 * 
 */
public interface ContentHolder extends Persistable {
    Collection<TcApplicationdata> application = null;
    //public TcDomain getDomain();
    //public void setDomain(TcDomain domain);
}
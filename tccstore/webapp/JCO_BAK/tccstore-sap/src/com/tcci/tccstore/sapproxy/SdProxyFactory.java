package com.tcci.tccstore.sapproxy;

import com.tcci.tccstore.sapproxy.enums.SapSystemEnum;
import com.tcci.tccstore.sapproxy.jco.SdProxy4JCo;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 依傳入參數產生 SDProxy, 包含OSB, JCO
 * 
 * @author Neo.Fu
 */
public class SdProxyFactory {

    protected static final Logger logger = LoggerFactory.getLogger(SdProxyFactory.class);

    /**
     * 產生SDProxy實體物件。
     * @param configProps 連結參數。
     * @return 
     */
    public static SdProxy createProxy(Properties configProps) {
        if (null == configProps) {
            throw new RuntimeException("configProps is null");
        }

        SdProxy ppProxy = null;
        if (SapSystemEnum.OSB.equals(getSapRoute(configProps))) {
//            return new Sd4OsbProxy();
            ppProxy = null; 
        } else if (SapSystemEnum.JCO.equals(getSapRoute(configProps))) {
//            return new SD4JCOProxy(configProps);
            SdProxy4JCo pp4JcoProxy = new SdProxy4JCo();
            pp4JcoProxy.init(configProps);
            ppProxy = pp4JcoProxy;
        } else {
            ppProxy = null;
        }

        if (null == ppProxy) {
            String msg = "can not get ppProxy!";
            logger.error(msg);
            throw new RuntimeException(msg);
        }
        logger.debug(ppProxy.getClass().getName() + " object created!");
        return ppProxy;
    }

    /**
     * 取得使用SAP的路徑("JCO"/"OSB")，預設為JCO
     * @return 
     */
    public static SapSystemEnum getSapRoute(Properties props) {
        String str = props.getProperty("sap.route");
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("configProps: sap.route is null");
        }
        if (StringUtils.startsWithIgnoreCase(str, "OSB")) {
            return SapSystemEnum.OSB;
        }
        return SapSystemEnum.JCO;
    }
}

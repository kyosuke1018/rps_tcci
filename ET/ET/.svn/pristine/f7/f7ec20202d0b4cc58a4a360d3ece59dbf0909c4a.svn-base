/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.admin;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.et.enums.CacheTypeEnum;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author peter.pan
 */
@Stateless
public class CmCacheFacade {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * 刪除 Cache
     * @param primaryType
     * @param primaryId
     * @param operator 
     */
    public void deleteCache(String primaryType, Long primaryId, TcUser operator){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        // 先刪除
        sql.append("DELETE FROM CM_CACHE_DATA WHERE PRIMARYTYPE=#PRIMARYTYPE ");
        if( primaryId!=null ){
            sql.append("AND PRIMARYID=#PRIMARYID");
            params.put("PRIMARYID", primaryId);
        }
        params.put("PRIMARYTYPE", primaryType);
        params.put("MODIFIER", operator.getId());
        
        logger.debug("deleteCache sql = \n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        for(String key : params.keySet()){
            q.setParameter(key, params.get(key));
            logger.debug(key + "=" + params.get(key));
        }

        q.executeUpdate();
    }
    
    /**
     * 更新自訂 Cache
     * @param primaryType
     * @param primaryId
     * @param operator 
     */
    public void refreshCache(String primaryType, Long primaryId, TcUser operator){
        // 先刪除
        deleteCache(primaryType, primaryId, operator);
        
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql;

        params.put("PRIMARYTYPE", primaryType);
        params.put("MODIFIER", operator.getId());
        
        // 更新
        if( CacheTypeEnum.AWARD.getCode().equalsIgnoreCase(primaryType) ){
            // 授獎等級/單位
            //renewCacheAward(primaryType, primaryId, operator);
        }
    }
    
/*
// 授獎等級/單位
DELETE FROM CM_CACHE_DATA WHERE PRIMARYTYPE='AW' AND PRIMARYID=0
-- 
INSERT INTO CM_CACHE_DATA (PRIMARYTYPE, PRIMARYID, VALUE, MODIFIER, MODIFYTIMESTAMP)
SELECT 'AW', S.ACCESSIONID, S.LISTSTR, NULL, NULL
FROM (
    SELECT t1.ACCESSIONID,
    STUFF(( 
            SELECT DISTINCT ',' + t2.CNAME
            FROM (
                    SELECT AO.ACCESSIONID, AO.OPTIONID, PO.CNAME, PO.ENAME
                    FROM KB_ACCESSION_OPTIONS AO 
                    JOIN KB_PLANTOPTIONS PO ON PO.ID=AO.OPTIONID AND PO.TYPE='A' 
            ) t2
            WHERE t1.ACCESSIONID = t2.ACCESSIONID
            FOR XML PATH(''), TYPE
    ).value('.', 'NVARCHAR(MAX)')
    ,1,1,'') LISTSTR
    FROM (
            SELECT AO.ACCESSIONID, AO.OPTIONID, PO.CNAME, PO.ENAME
            FROM KB_ACCESSION_OPTIONS AO 
            JOIN KB_PLANTOPTIONS PO ON PO.ID=AO.OPTIONID AND PO.TYPE='A' 
    ) t1
    WHERE 1=1
    AND t1.ACCESSIONID=0
    GROUP BY t1.ACCESSIONID
) S   

//  植物封面圖(for PDA)
-- ----------
DELETE FROM CM_CACHE_DATA WHERE PRIMARYTYPE='PI' AND PRIMARYID=0
-- 
INSERT INTO CM_CACHE_DATA (PRIMARYTYPE, PRIMARYID, VALUE, MODIFIER, MODIFYTIMESTAMP)
SELECT 'PI', S.PRIMARYID, STR(S.PID), NULL, NULL
FROM (
	select P.PRIMARYID, MAX(P.ID) PID
	from KB_PHOTO_GALLERY P 
	where P.PRIMARYTYPE='P' AND P.STATUS='P'
	GROUP BY P.PRIMARYID
) S	

INSERT INTO CM_CACHE_DATA (PRIMARYTYPE, PRIMARYID, VALUE, MODIFIER, MODIFYTIMESTAMP)
SELECT 'PI', S.PRIMARYID, STR(S.PID), NULL, NULL
FROM (
	select P.PRIMARYID, MAX(P.ID) PID
	from KB_PHOTO_GALLERY P 
	where P.PRIMARYTYPE='P' -- AND P.STATUS='P'
	and not exists (
		select * from CM_CACHE_DATA where PRIMARYTYPE='PI' and PRIMARYID=P.PRIMARYID
	)
	GROUP BY P.PRIMARYID
) S
    
-- -----------------
// 往來對象身分別
DELETE FROM CM_CACHE_DATA WHERE PRIMARYTYPE='CT' AND PRIMARYID=0
-- 
INSERT INTO CM_CACHE_DATA (PRIMARYTYPE, PRIMARYID, VALUE, MODIFIER, MODIFYTIMESTAMP)
SELECT 'CT', S.CONTACTSID, S.LISTSTR, NULL, NULL
FROM (
    SELECT t1.CONTACTSID,
    STUFF(( 
            SELECT DISTINCT ',' + t2.TYPENAME
            FROM (
                    SELECT CT.CONTACTSID, CT.TYPE
				, CASE WHEN CT.TYPE='C' THEN '採集者'
				    WHEN CT.TYPE='S' THEN '賣方'
				    WHEN CT.TYPE='B' THEN '購買者'
				    WHEN CT.TYPE='D' THEN '捐贈者'
				    WHEN CT.TYPE='R' THEN '接受者'
				    WHEN CT.TYPE='P' THEN '授粉者'
				    WHEN CT.TYPE='G' THEN '播種者'
				    WHEN CT.TYPE='I' THEN '鑑定者'
				    WHEN CT.TYPE='F' THEN '保種中心成員'
				    END TYPENAME
                    FROM KB_CONTACTS_TYPE CT 
            ) t2
            WHERE t1.CONTACTSID = t2.CONTACTSID
            FOR XML PATH(''), TYPE
    ).value('.', 'NVARCHAR(MAX)')
    ,1,1,'') LISTSTR
    FROM (
        SELECT CT.CONTACTSID, CT.TYPE
	   , CASE WHEN CT.TYPE='C' THEN '採集者'
		  WHEN CT.TYPE='S' THEN '賣方'
		  WHEN CT.TYPE='B' THEN '購買者'
		  WHEN CT.TYPE='D' THEN '捐贈者'
		  WHEN CT.TYPE='R' THEN '接受者'
		  WHEN CT.TYPE='P' THEN '授粉者'
		  WHEN CT.TYPE='G' THEN '播種者'
		  WHEN CT.TYPE='I' THEN '鑑定者'
		  WHEN CT.TYPE='F' THEN '保種中心成員'
		  END TYPENAME
        FROM KB_CONTACTS_TYPE CT 
    ) t1
    WHERE 1=1
    -- AND t1.CONTACTSID=0
    GROUP BY t1.CONTACTSID
) S   
    
-- -----------------
// 維管工作項目
DELETE FROM CM_CACHE_DATA WHERE PRIMARYTYPE='WI' -- AND PRIMARYID=0
-- 
INSERT INTO CM_CACHE_DATA (PRIMARYTYPE, PRIMARYID, VALUE, MODIFIER, MODIFYTIMESTAMP)
SELECT 'WI', S.MID, S.LISTSTR, NULL, NULL
FROM (
    SELECT t1.MID,
    STUFF(( 
            SELECT DISTINCT ',' + t2.CNAME
            FROM (
                SELECT W.MID, W.OPID, OP.CNAME
                FROM KB_OP_WORKS W 
                JOIN KB_PLANTOPTIONS OP ON OP.ID=W.OPID AND OP.TYPE='W'
            ) t2
            WHERE t1.MID = t2.MID
            FOR XML PATH(''), TYPE
    ).value('.', 'NVARCHAR(MAX)')
    ,1,1,'') LISTSTR
    FROM (
        SELECT W.MID, W.OPID, OP.CNAME
        FROM KB_OP_WORKS W 
        JOIN KB_PLANTOPTIONS OP ON OP.ID=W.OPID AND OP.TYPE='W'
    ) t1
    WHERE 1=1
    -- AND t1.MID=0
    GROUP BY t1.MID
) S    
*/    
}

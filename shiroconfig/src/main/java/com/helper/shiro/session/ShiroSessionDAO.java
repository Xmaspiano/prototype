package com.helper.shiro.session;

import com.alibaba.fastjson.JSONObject;
import com.helper.shiro.util.SerializeUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/30 - 上午10:00
 * Created by IntelliJ IDEA.
 */
public class ShiroSessionDAO extends AbstractSessionDAO{
    private static Logger logger = LoggerFactory.getLogger(ShiroSessionDAO.class);
    private ObejctManager obejctManager;
    private int expire = 1200;
    public String keyPrefix = "shiro_redis_session:";

    public ShiroSessionDAO(ObejctManager obejctManager){
        this.obejctManager = obejctManager;
    }

    /**
     * Subclass implementation hook to actually persist the {@code Session}'s state to the underlying EIS.
     *
     * @param session the session object whose state will be propagated to the EIS.
     */
//    @Override
    protected void doUpdate(Session session) {
        this.saveSession(session);
    }

    /**
     * Subclass implementation hook to permanently delete the given Session from the underlying EIS.
     *
     * @param session the session instance to permanently delete from the EIS.
     */
//    @Override
    protected void doDelete(Session session) {
        if (session != null && session.getId() != null) {
            obejctManager.del(this.getByteKey(session.getId()));
        } else {
            logger.error("session or session id is null");
        }
    }

    /**
     * Subclass hook to actually persist the given <tt>Session</tt> instance to the underlying EIS.
     *
     * @param session the Session instance to persist to the EIS.
     * @return the id of the session created in the EIS (i.e. this is almost always a primary key and should be the
     * value returned from {@link Session#getId() Session.getId()}.
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        System.out.println("==123==");
        System.out.println(JSONObject.toJSON(session));
        this.saveSession(session);
        return sessionId;
    }

    /**
     * Subclass implementation hook that retrieves the Session object from the underlying EIS or {@code null} if a
     * session with that ID could not be found.
     *
     * @param sessionId the id of the <tt>Session</tt> to retrieve.
     * @return the Session in the EIS identified by <tt>sessionId</tt> or {@code null} if a
     * session with that ID could not be found.
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("doReadSession !!!");
        System.out.println("obejctManager:"+obejctManager);
        if (sessionId == null) {
            logger.error("session id is null");
            return null;
        } else {
            Session s = (Session) SerializeUtils.deserialize(obejctManager.get(this.getByteKey(sessionId)));
            System.out.println(s instanceof Session);
            return s;
        }
    }

    protected void saveSession(Session session) throws UnknownSessionException {
        if (session != null && session.getId() != null) {
            System.out.println("=====>>123<<=====");
            System.out.println(JSONObject.toJSON(session));

            byte[] key = this.getByteKey(session.getId());
            byte[] value = SerializeUtils.serialize(session);

            session.setTimeout(expire * 1000);

            System.out.println("=====>><<=====");
            System.out.println(String.valueOf(key)+":"+String.valueOf(value));

            this.obejctManager.set(key, value, expire);

        } else {
            logger.error("session or session id is null");
        }
    }

    public byte[] getByteKey(Serializable sessionId){
        String preKey = this.keyPrefix + sessionId;
        return preKey.getBytes();
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    /**
     * Updates (persists) data from a previously created Session instance in the EIS identified by
     * {@code {@link Session#getId() session.getId()}}.  This effectively propagates
     * the data in the argument to the EIS record previously saved.
     * <p/>
     * In addition to UnknownSessionException, implementations are free to throw any other
     * exceptions that might occur due to integrity violation constraints or other EIS related
     * errors.
     *
     * @param session the Session to update
     * @throws UnknownSessionException if no existing EIS session record exists with the
     *                                 identifier of {@link Session#getId() session.getSessionId()}
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        doUpdate(session);
    }

    /**
     * Deletes the associated EIS record of the specified {@code session}.  If there never
     * existed a session EIS record with the identifier of
     * {@link Session#getId() session.getId()}, then this method does nothing.
     *
     * @param session the session to delete.
     */
    @Override
    public void delete(Session session) {
        doDelete(session);
    }

    /**
     * Returns all sessions in the EIS that are considered active, meaning all sessions that
     * haven't been stopped/expired.  This is primarily used to validate potential orphans.
     * <p/>
     * If there are no active sessions in the EIS, this method may return an empty collection or {@code null}.
     * <h4>Performance</h4>
     * This method should be as efficient as possible, especially in larger systems where there might be
     * thousands of active sessions.  Large scale/high performance
     * implementations will often return a subset of the total active sessions and perform validation a little more
     * frequently, rather than return a massive set and validate infrequently.  If efficient and possible, it would
     * make sense to return the oldest unstopped sessions available, ordered by
     * {@link Session#getLastAccessTime() lastAccessTime}.
     * <h4>Smart Results</h4>
     * <em>Ideally</em> this method would only return active sessions that the EIS was certain should be invalided.
     * Typically that is any session that is not stopped and where its lastAccessTimestamp is older than the session
     * timeout.
     * <p/>
     * For example, if sessions were backed by a relational database or SQL-92 'query-able' enterprise cache, you might
     * return something similar to the results returned by this query (assuming
     * {@link SimpleSession SimpleSession}s were being stored):
     * <pre>
     * select * from sessions s where s.lastAccessTimestamp < ? and s.stopTimestamp is null
     * </pre>
     * where the {@code ?} parameter is a date instance equal to 'now' minus the session timeout
     * (e.g. now - 30 minutes).
     *
     * @return a Collection of {@code Session}s that are considered active, or an
     * empty collection or {@code null} if there are no active sessions.
     */
    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet();
        Set<byte[]> keys = obejctManager.keys((this.keyPrefix + "*"));
        if (keys != null && keys.size() > 0) {
            Iterator iterator = keys.iterator();

            while(iterator.hasNext()) {
                byte[] key = (byte[])iterator.next();
                Session s = (Session) SerializeUtils.deserialize(obejctManager.get(key));
                sessions.add(s);
            }
        }
        return sessions;
    }

//    /**
//     * Sets the available CacheManager instance on this component.
//     *
//     * @param cacheManager the CacheManager instance to set on this component.
//     */
//    @Override
//    public void setCacheManager(ObejctManager cacheManager) {
//        this.obejctManager = cacheManager;
//    }
}

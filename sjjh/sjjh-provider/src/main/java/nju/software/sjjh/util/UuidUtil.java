package nju.software.sjjh.util;

import java.util.UUID;

/**
 * Uuid工具类
 * @author wzq
 *
 */
public class UuidUtil {

	public synchronized static String generateUuid(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString().toUpperCase();
	}

}

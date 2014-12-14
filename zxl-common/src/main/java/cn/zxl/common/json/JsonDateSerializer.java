package cn.zxl.common.json;

import java.io.IOException;
import java.util.Date;

import net.sf.json.JSONObject;
import cn.zxl.common.BeanUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonDateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date date, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
		if (!BeanUtil.isEmpty(date)) {
			generator.writeObject(JSONObject.fromObject(date));
		}
	}

}

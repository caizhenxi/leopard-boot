package io.leopard.boot.xparam.resolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * List<?>参数解析.
 * 
 * @author 阿海
 *
 */
@Component
@Order(4)
public class ParamListHandlerMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver implements XParamResolver {

	protected static Log logger = LogFactory.getLog(ParamListHandlerMethodArgumentResolver.class);

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> type = parameter.getParameterType();
		if (!type.equals(List.class)) {
			return false;
		}
		String name = parameter.getParameterName();
		return name.endsWith("List");
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request.getNativeRequest();

		return resolveListParameter(name, parameter, req);
	}

	public static Object resolveListParameter(String name, MethodParameter parameter, HttpServletRequest request) {
		String[] values = getParameterValues(request, name);
		// logger.info("name:" + name + " values:" + values);
		if (values == null) {
			return null;
		}
		if (values.length == 1) {
			if (StringUtils.isEmpty(values[0])) {
				return null;
			}
			else if (values[0].startsWith("[") && values[0].endsWith("]")) {
				Type arg = ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments()[0];

				if (arg instanceof ParameterizedType) {
					// System.out.println("ParameterizedType:" + parameter.getGenericParameterType());
					return UnderlineJson.toListObject(values[0], parameter.getGenericParameterType());
				}
				else {
					Class<?> clazz = (Class<?>) arg;
					// System.out.println("arg:" + arg.getClass().getName() + " clazz:" + clazz.getName());
					return UnderlineJson.toListObject(values[0], clazz);
				}
				// }
				// else {
				// Class<?> clazz = (Class<?>) args[0];
				// Class<?> subClazz = (Class<?>) args[1];
				// return UnderlineJson.toListSubParametrizedType(values[0], clazz, subClazz);
				// }
			}
			else {
				logger.info("values[0]:" + values[0]);
				return values[0].split(",");
			}
		}
		return values;

		// int hashCode = parameter.hashCode();
		// Class<?> clazz = modelMap.get(hashCode);
		// if (clazz != null) {
		// return toList(clazz, values);
		// }
		//
		// if (values != null && values.length == 1) {
		// // logger.info("values:" + values[0]);
		// if (StringUtils.isEmpty(values[0])) {
		// return null;
		// }
		// // TODO 暂时只支持List<String>
		// if (values[0].startsWith("[") && values[0].endsWith("]")) {
		// return Json.toListObject(values[0], String.class);
		// }
		// }
		// return values;
	}

	public static String[] getParameterValues(HttpServletRequest req, String name) {
		String[] values = req.getParameterValues(name);
		// logger.info("name:" + name + " values:" + values);
		if (values == null) {
			values = req.getParameterValues(name.replaceFirst("List$", ""));
			if (values == null) {
				return null;
			}
		}
		return values;
	}

}

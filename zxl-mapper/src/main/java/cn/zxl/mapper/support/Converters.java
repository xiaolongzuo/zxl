package cn.zxl.mapper.support;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.zxl.common.DateUtil;
import cn.zxl.common.ReflectUtil;
import cn.zxl.mapper.MapContext;
import cn.zxl.mapper.config.Relation;

public class Converters {

	private Converters() {
	}

	private static void check(Class<?> leftType, Class<?> rightType, Relation relation) {
		if (leftType == null || rightType == null) {
			throw new NullPointerException("sourceType or targetType is null ");
		}
		if (relation == null) {
			throw new NullPointerException("relation is null ");
		}
	}

	public static Converter getConverter(Class<?> leftType, Class<?> rightType, Relation relation) {
		check(leftType, rightType, relation);
		Class<?> leftFieldClass;
		Class<?> rightFieldClass;
		try {
			leftFieldClass = ReflectUtil.getAllField(leftType, relation.getLeftName()).getType();
			rightFieldClass = ReflectUtil.getAllField(rightType, relation.getRightName()).getType();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (leftFieldClass == rightFieldClass) {
			return getDefaultConverter(relation);
		} else {
			Method[] methods = Converters.class.getDeclaredMethods();
			Method method = null;
			String methodName = "get" + leftFieldClass.getSimpleName() + "To" + rightFieldClass.getSimpleName() + "Converter";
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equals(methodName)) {
					method = methods[i];
					break;
				}
			}
			if (method == null) {
				throw new IllegalArgumentException(methodName + " can't be found");
			}
			try {
				return (Converter) method.invoke(null, new Object[] { relation });
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static Converter getReverseConverter(final Converter converter) {
		return new Converter() {

			@Override
			public Object doLeftToRight(MapContext context) {
				return converter.rightToLeft(context);
			}

			@Override
			public Object doRightToLeft(MapContext context) {
				return converter.leftToRight(context);
			}

		};
	}

	public static Converter getDefaultConverter(final Relation relation) {
		return DefaultConverter.INSTANCE;
	}

	public static class DefaultConverter extends Converter {

		private static final DefaultConverter INSTANCE = new DefaultConverter();

		private DefaultConverter() {
		}

		@Override
		public Object doLeftToRight(MapContext context) {
			return context.getSourceFieldValue();
		}

		@Override
		public Object doRightToLeft(MapContext context) {
			return context.getSourceFieldValue();
		}

	}

	public static Converter getStringToDateConverter(final Relation relation) {
		return new Converter() {

			@Override
			public Object doLeftToRight(MapContext context) {
				try {
					String dateFormat = relation.getDateFormat();
					if (dateFormat != null) {
						return new SimpleDateFormat(dateFormat).parse(context.getSourceFieldValue().toString());
					} else {
						return DateUtil.parse(context.getSourceFieldValue().toString());
					}
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public Object doRightToLeft(MapContext context) {
				String dateFormat = relation.getDateFormat();
				if (dateFormat != null) {
					return new SimpleDateFormat(dateFormat).format(context.getSourceFieldValue());
				} else {
					return DateUtil.timestampFormat((Date) context.getSourceFieldValue());
				}
			}

		};
	}

	public static Converter getDateToStringConverter(final Relation relation) {
		return getReverseConverter(getStringToDateConverter(relation));
	}

	public static Converter getIntegerToStringConverter(final Relation relation) {
		return IntegerToStringConverter.INSTANCE;
	}

	public static Converter getStringToIntegerConverter(final Relation relation) {
		return getReverseConverter(IntegerToStringConverter.INSTANCE);
	}

	public static class IntegerToStringConverter extends Converter {

		private static final IntegerToStringConverter INSTANCE = new IntegerToStringConverter();

		private IntegerToStringConverter() {
		}

		@Override
		public Object doLeftToRight(MapContext context) {
			return context.getSourceFieldValue() == null ? null : context.getSourceFieldValue().toString();
		}

		@Override
		public Object doRightToLeft(MapContext context) {
			return context.getSourceFieldValue() == null ? null : Integer.valueOf(context.getSourceFieldValue().toString());
		}

	}

}

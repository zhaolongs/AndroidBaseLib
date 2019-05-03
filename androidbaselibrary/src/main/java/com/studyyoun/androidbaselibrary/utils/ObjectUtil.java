package com.studyyoun.androidbaselibrary.utils;

import com.studyyoun.androidbaselibrary.model.BaseBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ObjectUtil {

	/***
	 * 依据class的名称获取对应class
	 *
	 *            非基本数据类型的任意类型
	 * @param classAllName
	 *            类的全称(如: java.lang.String)
	 * @return 返回依据类名映射的class对象
	 */
	@SuppressWarnings("unchecked")
	public static Class getClassByName(String classAllName) {
		try {
			return Class.forName(classAllName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 初始化对象
	 * 
	 * @param clazz
	 *            创建的对象的类型
	 * @param attrMap
	 *            初始对象的属性值
	 * @return 创建的对象
	 */
	public static <T> T initObject(Class<T> clazz, Map<String, Object> attrMap) {
		try {
			T obj = clazz.newInstance();
			if (attrMap != null) {
				for (String attrName : attrMap.keySet()) {
					if (!attrName.equals("serialVersionUID")) {
						setAttribute(obj, attrName, attrMap.get(attrName));
					}
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 给对象的属性赋值
	 * 
	 * @param obj
	 *            对象
	 * @param attrName
	 *            对象的属性名
	 * @param value
	 *            对象的属性值
	 */
	@SuppressWarnings("unchecked")
	public static void setAttribute(Object obj, String attrName, Object value) {
		try {
			Class clazz = obj.getClass();
			while (!clazz.equals(Object.class)) {
				try {
					Field f = clazz.getDeclaredField(attrName);
					f.setAccessible(true);
					f.set(obj, parseToObject(value, f.getType()));
					f.setAccessible(false);
					return;
				} catch (NoSuchFieldException e) {
					clazz = clazz.getSuperclass();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从对象中取值
	 * 
	 * @param obj
	 *            对象
	 * @param attrName
	 *            要取值的属性名
	 * @return 值
	 */
	@SuppressWarnings("unchecked")
	public static Object getAttributeValue(Object obj, String attrName) {
		try {
			Class clazz = obj.getClass();
			while (!clazz.equals(Object.class)) {
				try {
					Field f = clazz.getDeclaredField(attrName);
					f.setAccessible(true);
					Object value = f.get(obj);
					f.setAccessible(false);
					return value;
				} catch (NoSuchFieldException e) {
					clazz = clazz.getSuperclass();
				}
			}

			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	* getClassAnnotation(获取类的注解)   
	* (这里描述这个方法适用条件 – 可选)   
	* @param aclass
	* @return    
	* @return Annotation[]   
	* @exception    
	* @since  1.0.0
	 */
	public static List<Annotation> getClassAnnotation(Class<?> aclass) {
		List<Annotation> aList = new ArrayList<Annotation>();
		try {
			for (Class clazz = aclass; !clazz.equals(Object.class); clazz = clazz
					.getSuperclass()) {
				Annotation[] classAnnotations = clazz.getDeclaredAnnotations();
				for (Annotation f : classAnnotations) {
					// 子类最大，父类值不覆盖子类
					if (aList.contains(f)) {
						continue;
					}
					
					aList.add(f);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return aList;
	}
	
	/**
	 * 获取对象中的所有属性
	 * 
	 * @param <T>
	 * @param bean
	 *            对象
	 * @return 属性和值(Map[属性名, 属性值])
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> getFields(Object bean) {
		try {
			Map<String, T> map = new HashMap<String, T>();
			for (Class clazz = bean.getClass(); !clazz.equals(Object.class); clazz = clazz
					.getSuperclass()) {
				Field[] fs = clazz.getDeclaredFields();
				for (Field f : fs) {
					// 子类最大，父类值不覆盖子类
					if (map.containsKey(f.getName())) {
						continue;
					}
					f.setAccessible(true);
					Object value = f.get(bean);
					f.setAccessible(false);
					map.put(f.getName(), (T) value);
				}
			}
			map.remove("serialVersionUID");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取对象中的所有属性
	 *
	 * @return 属性和值(Map[属性名, 属性值])
	 */
	@SuppressWarnings("unchecked")
	public static List<Method> getMethods(Class<?> aclass) {
		List<Method> fList = new ArrayList<Method>();
		try {
			for (Class clazz = aclass; !clazz.equals(Object.class); clazz = clazz
					.getSuperclass()) {
				Method[] fs = clazz.getDeclaredMethods();
				for (Method f : fs) {
					// 子类最大，父类值不覆盖子类
					if (fList.contains(f)) {
						continue;
					}
					fList.add(f);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fList;
	}
	
	/**
	 * 获取对象中的所有属性
	 *
	 * @return 属性和值(Map[属性名, 属性值])
	 */
	@SuppressWarnings("unchecked")
	public static List<Field> getFields(Class<?> aclass) {
		List<Field> fList = new ArrayList<Field>();
		try {
			for (Class clazz = aclass; !clazz.equals(Object.class); clazz = clazz
					.getSuperclass()) {
				Field[] fs = clazz.getDeclaredFields();
				for (Field f : fs) {
					// 子类最大，父类值不覆盖子类
					if (fList.contains(f)) {
						continue;
					}
					fList.add(f);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fList;
	}

	/**
	 * 获取类的所有属性与属性的类型
	 * 
	 * @param clazz
	 *            类
	 * @return 该类的所有属性名与属性类型(包含父类属性)
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Class> getFieldNames(Class clazz) {
		try {
			Map<String, Class> attrMap = new HashMap<String, Class>();
			for (; !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
				Field[] fs = clazz.getDeclaredFields();
				for (Field f : fs) {
					attrMap.put(f.getName(), f.getType());
				}
			}
			attrMap.remove("serialVersionUID");
			return attrMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取对象中的非空属性(属性如果是对象，则只会在同一个map中新增，不会出现map嵌套情况)
	 * 
	 * @param bean
	 *            对象
	 * @param hasInitValue
	 *            是否过滤掉初始值(true:过滤掉)
	 * @return 非空属性和值(Map[属性名, 属性值])
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getNotNullFields(Object bean,
                                                       boolean hasInitValue) {
		try {
			if (hasInitValue) {
				cleanInitValue(bean);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			for (Class clazz = bean.getClass(); !clazz.equals(Object.class); clazz = clazz
					.getSuperclass()) {
				Field[] fs = clazz.getDeclaredFields();
				for (Field f : fs) {
					// 子类最大，父类值不覆盖子类
					if (map.containsKey(f.getName())) {
						continue;
					}
					f.setAccessible(true);
					Object value = f.get(bean);
					f.setAccessible(false);
					if (value != null) {
						map.put(f.getName(), value);
					}
				}
			}
			map.remove("serialVersionUID");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取对象中的非空属性(属性如果是对象，则只会在同一个map中新增，不会出现map嵌套情况) (不会清空初始值)
	 * 
	 * @param bean
	 *            对象
	 * @return 非空属性和值(Map[属性名, 属性值])
	 */
	public static Map<String, Object> getNotNullFields(Object bean) {
		return getNotNullFields(bean, true);
	}

	/**
	 * 获取对象中的非空属性(属性如果是对象，则会嵌套map)
	 * 
	 * @param bean
	 *            对象
	 * @return 非空属性和值(Map[属性名, 属性值])
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getNotNullFieldsForStructure(Object bean) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			for (Class clazz = bean.getClass(); !clazz.equals(Object.class); clazz = clazz
					.getSuperclass()) {
				Field[] fs = clazz.getDeclaredFields();
				for (Field f : fs) {
					// 子类最大，父类值不覆盖子类
					if (map.containsKey(f.getName())) {
						continue;
					}
					f.setAccessible(true);
					Object value = f.get(bean);
					f.setAccessible(false);
					if (value != null) {
						if (!isNotStructure(value)) {
							map.put(f.getName(),
									getNotNullFieldsForStructure(value));
						} else {
							map.put(f.getName(), value);
						}
					}
				}
			}
			map.remove("serialVersionUID");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/***
	 * 依据类，获取该类的泛型class
	 *
	 * @return 泛型类型
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> Class<T> getGeneric(Class clazz) {
		try {
			Type genType = clazz.getGenericSuperclass();
			if (!(genType instanceof ParameterizedType)) {
				return (Class<T>) Object.class;
			}
			Type[] params = ((ParameterizedType) genType)
					.getActualTypeArguments();
			return (Class<T>) params[0];
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将byte字节转换成对象
	 * 
	 * @param bts
	 *            字节数据
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T parseByteForObj(byte[] bts) {
		ByteArrayInputStream input = new ByteArrayInputStream(bts);
		ObjectInputStream objectInput = null;
		try {
			objectInput = new ObjectInputStream(input);
			return (T) objectInput.readObject();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (objectInput != null) {
					objectInput.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * 转换类型
	 * 
	 * @param value
	 *            字符串的值
	 * @param type
	 *            要转换的类型
	 * @return 转换后的值
	 */
	@SuppressWarnings("unchecked")
	public static <T> Object parseToObject(Object value, Class<T> type) {
		if (value == null || type == String.class) {
			return value;
		}
		if (type == Character.class || type == char.class) {
			char[] chars = value.toString().toCharArray();
			return chars.length > 0 ? chars.length > 1 ? chars : chars[0]
					: Character.MIN_VALUE;
		}
		if (type == Boolean.class || type == boolean.class) {
			return Boolean.parseBoolean(value.toString());
		}
		// 处理boolean值转换
		Object oldValue = value;
		value = value.toString().equalsIgnoreCase("true") ? 1 : value
				.toString().equalsIgnoreCase("false") ? 0 : value;
		if (type == Long.class || type == long.class) {
			return Long.parseLong(value.toString());
		}
		if (type == BigDecimal.class) {
			return new BigDecimal(value.toString());
		}
		if (type == Integer.class || type == int.class) {
			return Integer.parseInt(value.toString());
		}
		if (type == Double.class || type == double.class) {
			return Double.parseDouble(value.toString());
		}
		if (type == Float.class || type == float.class) {
			return Float.parseFloat(value.toString());
		}
		if (type == Byte.class || type == byte.class) {
			return Byte.parseByte(value.toString());
		}
		if (type == Short.class || type == short.class) {
			return Short.parseShort(value.toString());
		}
		return (T) oldValue;
	}

	/***
	 * 是否非结构体(不再解析)
	 * 
	 * @param value
	 *            要验证数据
	 * @return 是否是结构体
	 */
	@SuppressWarnings("unchecked")
	private static boolean isNotStructure(Object value) {
		if (!isBaseClass(value)) {
			if (value instanceof BaseBean) {
				return false;
			} else if (value instanceof Collection) {
				return true;
			} else if (value instanceof Map) {
				return true;
			} else if (value instanceof Date) {
				return true;
			} else if (value.getClass().isArray()) {
				return true;
			}
			return false;
		}
		return true;
	}

	/***
	 * 校验是否是九种基础类型(即：非用户定义的类型)
	 * 
	 * @param value
	 *            字符串的值 要校验的值
	 * @return 是否是基础类型(true:已经是基础类型了)
	 */
	public static boolean isBaseClass(Object value) {
		if (value == null) {
			return true;
		} else if (value instanceof Long) {
			return true;
		} else if (value instanceof Integer) {
			return true;
		} else if (value instanceof Double) {
			return true;
		} else if (value instanceof Float) {
			return true;
		} else if (value instanceof Byte) {
			return true;
		} else if (value instanceof Boolean) {
			return true;
		} else if (value instanceof Short) {
			return true;
		} else if (value instanceof Character) {
			return true;
		} else if (value instanceof String) {
			return true;
		}
		return false;
	}

	/***
	 * 克隆有序列化的对象
	 * 
	 * @param <T>
	 *            要返回的数据类型
	 * @param bean
	 *            所有继承过BaseBean的对象
	 * @return 克隆后的对象
	 */
	public static <T> T CloneObject(Class<T> clazz, Object bean) {
		try {
			Map<String, Object> attrMap = getFields(bean);
			attrMap.remove("serialVersionUID");
			return initObject(clazz, attrMap);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/***
	 * 克隆有序列化的对象
	 * 
	 * @param <T>
	 *            要返回的数据类型
	 * @param bean
	 *            要克隆的对象
	 * @return 克隆后的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T CloneObject(T bean) {
		try {
			Map<String, Object> attrMap = getFields(bean);
			attrMap.remove("serialVersionUID");
			return (T) initObject(bean.getClass(), attrMap);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将新数据的非空属性值插入到基本数据中
	 * 
	 * @param baseData
	 *            基本数据
	 * @param newData
	 *            新数据
	 */
	public static void insertObj(BaseBean baseData, BaseBean newData) {
		try {
			if (baseData == null || newData == null) {
				return;
			}
			// 清空初始值
			Map<String, Object> attrList = getNotNullFields(newData);
			Set<String> keys = attrList.keySet();
			if (keys != null && keys.size() > 0) {
				for (String key : keys) {
					if (!key.equals("serialVersionUID")) {
						setAttribute(baseData, key, attrList.get(key));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 清空对象中所有属性的初始值 */
	public static <T> void cleanInitValue(T bean) {
		if (bean == null) {
			return;
		}
		try {
			Class<?> clazz = bean.getClass();
			Object obj = clazz.newInstance();
			for (; !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
				Field[] fs = clazz.getDeclaredFields();
				for (Field f : fs) {
					if (f.getName().equals("serialVersionUID")) {
						continue;
					}
					f.setAccessible(true);
					Object initValue = f.get(obj);
					Object oldValue = f.get(bean);
					if (initValue != null && initValue.equals(oldValue)) {
						f.set(bean, null);
					}
					f.setAccessible(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将对象转换为byte数据
	 * 
	 * @param obj
	 *            对象
	 * @return byte数据
	 */
	public static byte[] parseObjForByte(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}
}

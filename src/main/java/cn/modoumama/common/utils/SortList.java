package cn.modoumama.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("all")
public class SortList<E> {
	
	public void Sort(List<E> list, final String method, final String sort) {
		Collections.sort(list, new Comparator<E>() {
			public int compare(Object a, Object b) {
				double ret = 0;
				try {
					Method m1 = ((E) a).getClass().getMethod(method);
					Method m2 = ((E) b).getClass().getMethod(method);
					if (sort != null && "desc".equals(sort))// 倒序
						ret = Double.parseDouble(m2.invoke(((E) b)).toString())-Double.parseDouble(m1.invoke(((E) a)).toString());
					else
						// 正序  
						ret = Double.parseDouble(m1.invoke(((E) a)).toString())-Double.parseDouble(m2.invoke(((E) b)).toString());
				} catch (NoSuchMethodException ne) {
					System.out.println(ne);
				} catch (IllegalAccessException ie) {
					System.out.println(ie);
				} catch (InvocationTargetException it) {
					System.out.println(it);
				}
				return (int)ret;
			}
		});
	}
}

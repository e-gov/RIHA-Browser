package ee.ria.riha.domain.model;

import java.util.ArrayList;

import org.springframework.beans.factory.InitializingBean;

public class NationalHolidays extends ArrayList<NationalHoliday> implements InitializingBean {

	private static final long serialVersionUID = 3767557863050533677L;
	private static NationalHolidays instance;

	@Override
	public void afterPropertiesSet() {
		instance = this;
	}

	public static NationalHolidays get() {
		return instance;
	}
}

package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.MiscellaneousData;

@Component
@Transactional
public class MiscellaneousDataToStringConverter implements Converter<MiscellaneousData, String> {

	@Override
	public String convert(MiscellaneousData miscellaneousdata) {
		String result;

		if (miscellaneousdata == null) {
			result = null;
		} else {
			result = String.valueOf(miscellaneousdata.getId());
		}
		return result;
	}
}

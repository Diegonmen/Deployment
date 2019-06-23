package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.PersonalData;

@Component
@Transactional
public class PersonalDataToStringConverter implements Converter<PersonalData, String> {

	@Override
	public String convert(PersonalData personaldata) {
		String result;

		if (personaldata == null) {
			result = null;
		} else {
			result = String.valueOf(personaldata.getId());
		}
		return result;
	}
}

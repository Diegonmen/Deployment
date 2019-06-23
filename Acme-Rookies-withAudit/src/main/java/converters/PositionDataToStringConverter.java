package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.PositionData;

@Component
@Transactional
public class PositionDataToStringConverter implements Converter<PositionData, String> {

	@Override
	public String convert(PositionData positiondata) {
		String result;

		if (positiondata == null) {
			result = null;
		} else {
			result = String.valueOf(positiondata.getId());
		}
		return result;
	}
}

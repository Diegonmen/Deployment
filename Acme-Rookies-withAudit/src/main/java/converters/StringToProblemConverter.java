package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Problem;
import repositories.ProblemRepository;

@Component
@Transactional
public class StringToProblemConverter implements Converter<String, Problem> {

	@Autowired
	ProblemRepository problemRepository;

	@Override
	public Problem convert(String text) {
		Problem result;
		int id;

		try {
			if (StringUtils.isEmpty(text)) {
				result = null;
			} else {
				id = Integer.valueOf(text);
				result = problemRepository.findOne(id);
			}
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}

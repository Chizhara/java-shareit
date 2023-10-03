package ru.practicum.shareit.annotation;

import ru.practicum.shareit.interfaces.Interval;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IntervalValidator implements ConstraintValidator<IntervalConstraint, Interval> {

    @Override
    public boolean isValid(Interval interval,
                           ConstraintValidatorContext cxt) {
        if (interval.getStart() == null || interval.getEnd() == null) {
            return true;
        }
        return interval.getStart().isBefore(interval.getEnd());
    }
}

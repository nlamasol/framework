/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.ui.components.field;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQueries;

import org.jsoup.nodes.Element;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.typed.Handler;
import com.vaadin.event.typed.Registration;
import com.vaadin.shared.ui.components.field.DateFieldServerRpc;
import com.vaadin.shared.ui.components.field.DateFieldState;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.components.HasValue;
import com.vaadin.ui.declarative.DesignContext;

/**
 * A simple textual date input component.
 * 
 * @author Vaadin Ltd.
 */
public class DateField extends AbstractComponent
        implements HasValue<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("dd-MM-uuuu");

    public static class DateChange
            extends com.vaadin.event.typed.Event<LocalDate> {
        protected DateChange(DateField source, LocalDate date,
                boolean userOriginated) {
            super(source, date, userOriginated);
        }
    }

    public DateField() {
        registerRpc(new DateFieldServerRpc() {

            @Override
            public void blur() {
                fireEvent(new BlurEvent(DateField.this));
            }

            @Override
            public void focus() {
                fireEvent(new FocusEvent(DateField.this));
            }

            @Override
            public void setDate(String value) {
                if (!isReadOnly()) {
                    LocalDate localDate = FORMATTER.parse(value,
                            TemporalQueries.localDate());
                    setValue(localDate);
                    fireEvent(new DateChange(DateField.this, localDate, true));
                }
            }
        });
    }

    @Override
    protected DateFieldState getState() {
        return (DateFieldState) super.getState();
    }

    @Override
    protected DateFieldState getState(boolean markAsDirty) {
        return (DateFieldState) super.getState(markAsDirty);
    }

    @Override
    public void setValue(LocalDate date) {
        DateFieldState state = getState();
        state.value = date.format(FORMATTER);
    }

    @Override
    public LocalDate getValue() {
        DateFieldState state = getState(false);
        return FORMATTER.parse(state.value, TemporalQueries.localDate());
    }

    @Override
    public Registration onChange(Handler<LocalDate> handler) {
        return onEvent(DateChange.class, handler);
    }

    @Override
    public void readDesign(Element design, DesignContext designContext) {
        super.readDesign(design, designContext);
    }
}
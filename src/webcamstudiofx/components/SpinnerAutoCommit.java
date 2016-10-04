/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import javafx.collections.ObservableList;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

/**
 *
 * @author karl
 */
public class SpinnerAutoCommit<T> extends Spinner<T> {

        public SpinnerAutoCommit() {
            super();
            addListenerKeyChange();
        }

        public SpinnerAutoCommit(int min, int max, int initialValue) {
            super(min, max, initialValue);
            addListenerKeyChange();
        }

        public SpinnerAutoCommit(int min, int max, int initialValue, int amountToStepBy) {
            super(min, max, initialValue, amountToStepBy);
            addListenerKeyChange();
        }

        public SpinnerAutoCommit(double min, double max, double initialValue) {
            super(min, max, initialValue);
            addListenerKeyChange();
        }

        public SpinnerAutoCommit(double min, double max, double initialValue, double amountToStepBy) {
            super(min, max, initialValue, amountToStepBy);
            addListenerKeyChange();
        }

        public SpinnerAutoCommit(ObservableList<T> items) {
            super(items);
            addListenerKeyChange();
        }

        public SpinnerAutoCommit(SpinnerValueFactory<T> valueFactory) {
            super(valueFactory);
            addListenerKeyChange();
        }

        private void addListenerKeyChange() {
            getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                commitEditorText();
            });
        }

        private void commitEditorText() {
            if (!isEditable()) return;
            String text = getEditor().getText();
            SpinnerValueFactory<T> valueFactory = getValueFactory();
            if (valueFactory != null) {
                StringConverter<T> converter = valueFactory.getConverter();
                if (converter != null) {
                    T value = converter.fromString(text);
                    if (value != null)
                        valueFactory.setValue(value);
                }
            }
        }   
    }

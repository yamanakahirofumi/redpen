/**
 * redpen: a text inspection tool
 * Copyright (C) 2014 Recruit Technologies Co., Ltd. and contributors
 * (see CONTRIBUTORS.md)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.unigram.docvalidator.validator.section;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unigram.docvalidator.model.Paragraph;
import org.unigram.docvalidator.model.Section;
import org.unigram.docvalidator.model.Sentence;
import org.unigram.docvalidator.util.CharacterTable;
import org.unigram.docvalidator.util.ValidationError;
import org.unigram.docvalidator.util.ValidatorConfiguration;

/**
 * Validate the length of one section.
 */
public class SectionLengthValidator extends AbstractSectionValidator {
  /**
   * Constructor.
   */
  public SectionLengthValidator() {
    super();
  }

  @Override
  public List<ValidationError> validate(Section section) {
    List<ValidationError> validationErrors = new ArrayList<ValidationError>();
    int sectionCharNumber = 0;
    for (Iterator<Paragraph> paraIterator =
        section.getParagraphs(); paraIterator.hasNext();) {
      Paragraph currentPraParagraph = paraIterator.next();
      for (Iterator<Sentence> sentenceIterator =
          currentPraParagraph.getSentences(); sentenceIterator.hasNext();) {
        Sentence sentence = sentenceIterator.next();
        sectionCharNumber += sentence.content.length();
      }
      if (sectionCharNumber > maxSectionCharNumber) {
        ValidationError error = new ValidationError(
            this.getClass(),
            "The number of the character exceeds the maximum \""
                + String.valueOf(sectionCharNumber) + "\".",
                section.getHeaderContent(0));
        validationErrors.add(error);
      }
    }
    return validationErrors;
  }

  @Override
  public boolean loadConfiguration(ValidatorConfiguration conf,
      CharacterTable characterTable) {
    if (conf.getAttribute("max_char_number") == null) {
      this.maxSectionCharNumber = DEFAULT_MAXIMUM_CHAR_NUMBER_IN_A_SECTION;
      LOG.info("max_char_number was not set.");
      LOG.info("Using the default value of max_char_number.");
    } else {
      this.maxSectionCharNumber = Integer.valueOf(
          conf.getAttribute("max_char_number"));
    }
    return true;
  }

  protected void setMaxSectionLength(int max) {
    this.maxSectionCharNumber = max;
  }

  private static final int DEFAULT_MAXIMUM_CHAR_NUMBER_IN_A_SECTION = 1000;

  private static final Logger LOG =
      LoggerFactory.getLogger(SectionLengthValidator.class);

  private int maxSectionCharNumber;

}

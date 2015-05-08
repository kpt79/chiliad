/*
 * Copyright 2015 Kovacs Peter Tibor
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package chiliad.parser.pdf.output;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chiliad.parser.pdf.model.MPage;
import chiliad.parser.pdf.utils.IOUtils;

public class JSONOutputIT {

	final static Logger LOG = LoggerFactory.getLogger(JSONOutputIT.class);

	@Test
	public void testTryFindingTable() {
		MPage[] pages = IOUtils.readJSON(ClassLoader.getSystemResourceAsStream("dell-test-pages_pages_1_3.json"), MPage[].class);
		Assert.assertThat(pages.length, is(3));
	}
}

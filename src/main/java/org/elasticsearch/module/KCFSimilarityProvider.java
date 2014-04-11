package org.elasticsearch.module;

import com.kcf.analysis.Dictionary;
import org.apache.lucene.search.similarities.Similarity;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.similarity.AbstractSimilarityProvider;

/**
 * Author: 老牛 -- TK
 * Date:   14-4-4
 */
public class KCFSimilarityProvider extends AbstractSimilarityProvider {
    private static final ESLogger log = ESLoggerFactory.getLogger("kcfSimilarityProvider");

    private KCFSimilarity similarity;

    /**
     * Creates a new AbstractSimilarityProvider with the given name
     *
     * @param name Name of the Provider
     */
    @Inject
    public KCFSimilarityProvider(@Assisted String name, @Assisted Settings settings) {
        super(name);

        this.similarity = new KCFSimilarity(settings);

        Dictionary.init(settings);
    }

    @Override
    public Similarity get() {
        return this.similarity;
    }
}

package org.elasticsearch.module;

import com.kcf.analysis.Dictionary;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.settings.Settings;

/**
 * Author: 老牛 -- TK
 * Date:   14-4-4
 *
 * kcf`s similarity to rescore
 */
public class KCFSimilarity extends DefaultSimilarity {
    private static final ESLogger logger = ESLoggerFactory.getLogger("KCFSimilarity");

    private Settings settings;

    public KCFSimilarity(Settings settings){
        this.settings = settings;
    }

    @Override
    public Explanation idfExplain(CollectionStatistics collectionStats, TermStatistics termStats) {
        String field = collectionStats.field();
        String term = termStats.term().utf8ToString();

        Explanation explanation = super.idfExplain(collectionStats, termStats);

        if(explanation.isMatch()) {
            float originVal = explanation.getValue();
            explanation.setValue(originVal * this.calKCFValue(field, term));
        }else {
            logger.debug("no match: {}",explanation.toString());
        }

        return explanation;
    }

    @Override
    public Explanation idfExplain(CollectionStatistics collectionStats, TermStatistics[] termStats) {
        Explanation explanation =  super.idfExplain(collectionStats, termStats);
        float originVal = explanation.getValue();
        String field = collectionStats.field();

        if(termStats != null && termStats.length > 0 && explanation.isMatch()){
            for(TermStatistics termSt : termStats){
                String term = termSt.term().utf8ToString();

                originVal *=  this.calKCFValue(field, term);
            }
        }

        explanation.setValue(originVal);

        return explanation;
    }



    /**
     * use the given field and term to give a float
     * value as a weight value
     *
     * @param field
     * @param term
     * @return   an additional weight value
     */
    private float calKCFValue(String field, String term){
        float weight =  Dictionary.getWeight(term);

        logger.debug("field: {}, term: {}, weight: {}", field, term, weight);

        return weight;
    }
}

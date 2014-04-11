package org.elasticsearch.plugin;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.similarity.SimilarityModule;
import org.elasticsearch.module.KCFSimilarityProvider;
import org.elasticsearch.plugins.AbstractPlugin;

/**
 * User: xuyifeng
 * Date: 13-12-4
 * Time: 上午12:44
 */
public class KCFSimilarityPlugin extends AbstractPlugin{
    @Override
    public String name() {
        return "kcf-similarity";
    }

    @Override
    public String description() {
        return "kcf-similarity";
    }

    @Override
    public void processModule(Module module) {
        if(module instanceof SimilarityModule){
            SimilarityModule sm = (SimilarityModule) module;
            sm.addSimilarity("kcf-similarity", KCFSimilarityProvider.class);
        }
    }
}

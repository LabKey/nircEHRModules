Ext4.define('NIRC_EHR.data.ObsOrderChildClientStore', {
    extend: 'NIRC_EHR.data.ObsOrdersClientStore',

    insert: function(index, records) {
        var ret = this.callParent(arguments);

        // after insert of a new, track down the parent record to get the fields to inherit
        if (this.sectionCfg && this.sectionCfg.extraProperties && this.sectionCfg.extraProperties.parentQueryName) {
            var parentQuery = this.sectionCfg.extraProperties.parentQueryName;
            var parentStore = this.storeCollection.getClientStoreByName(parentQuery);
            var parentRecord = parentStore ? parentStore.getAt(0) : null;
            if (parentRecord && Ext4.isFunction(parentStore.onRecordUpdate)) {
                // get the set of inherited field names
                var inheritFieldNames = [];
                Ext4.each(this.getFields().items, function(field) {
                    if (field.inheritFromParent) {
                        inheritFieldNames.push(field.name);
                    }
                });

                parentStore.onRecordUpdate(parentRecord, inheritFieldNames);
            }
        }

        return ret;
    }

});
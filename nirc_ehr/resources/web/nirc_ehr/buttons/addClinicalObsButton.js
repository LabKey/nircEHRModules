
EHR.DataEntryUtils.registerGridButton('NIRC_DAILY_CLINICAL_OBS', function(config){
    return Ext4.Object.merge({
        text: 'Daily Observations',
        tooltip: 'Click to delete selected rows',
        handler: function(btn) {
            LABKEY.Query.selectRows({
                schemaName: 'ehr',
                queryName: 'observation_types',
                success: function (results) {
                    var grid = btn.up('gridpanel');
                    if (grid?.store?.data?.getCount() === 0) {
                        if (results?.rows?.length > 0) {
                            for (var i = 0; i < results.rows.length; i++) {
                                var row = results.rows[i];
                                if (row.value === 'Verified Id?' || row.value === 'Stool' || row.value === 'Activity' ||
                                        row.value === 'Appetite' || row.value === 'Hydration' || row.value === 'BCS') {

                                    var newRecord = grid.store.createModel({});
                                    newRecord.set({
                                        category: row.value,
                                    });
                                    grid.store.add(newRecord);
                                }
                            }
                        }
                    }
                },
                failure: function (error) {
                    Ext4.Msg.alert('Unable to load observation types. Please contact the system administrator.');
                    console.log('Error loading observation types: ' + error);
                },
                scope: this
            });
        }
    }, config);
});

EHR.DataEntryUtils.registerGridButton('NIRC_DAILY_CLINICAL_OBS_ORDERS', function(config){
    return Ext4.Object.merge({
        text: 'Daily Observations',
        tooltip: 'Click to delete selected rows',
        handler: function(btn) {
            LABKEY.Query.selectRows({
                schemaName: 'ehr_lookups',
                queryName: 'treatment_frequency',
                columns: 'rowid',
                filterArray: [LABKEY.Filter.create('meaning', 'SID', LABKEY.Filter.Types.EQUAL)],
                scope: this,
                success: function (results) {
                    if (results?.rows?.length > 0) {
                        const sidRowId = results.rows[0].rowid;
                        LABKEY.Query.selectRows({
                            schemaName: 'ehr',
                            queryName: 'observation_types',
                            success: function (results) {
                                var grid = btn.up('gridpanel');
                                if (grid?.store?.data?.getCount() === 0) {
                                    if (results?.rows?.length > 0) {
                                        for (var i = 0; i < results.rows.length; i++) {
                                            var row = results.rows[i];
                                            if (row.value === 'Verified Id?' || row.value === 'Stool' || row.value === 'Activity' ||
                                                    row.value === 'Appetite' || row.value === 'Hydration' || row.value === 'BCS') {

                                                var newRecord = grid.store.createModel({});
                                                newRecord.set({
                                                    category: row.value,
                                                    frequency: sidRowId
                                                });
                                                grid.store.add(newRecord);
                                            }
                                        }
                                    }
                                }
                            },
                            failure: function (error) {
                                Ext4.Msg.alert('Unable to load observation types. Please contact the system administrator.');
                                console.log('Error loading observation types: ' + error);
                            },
                            scope: this
                        });
                    }
                },
                failure: function (error) {
                    Ext4.Msg.alert('Unable to load treatment frequencies. Please contact the system administrator.');
                    console.log('Error loading treatment frequencies: ' + error);
                }
            });

        }
    }, config);
});
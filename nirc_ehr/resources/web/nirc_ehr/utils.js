NIRC_EHR.Utils = new function () {
    return {

        splitIds: function (subjectArray, unsorted, preserveDuplicates) {
            if (!subjectArray) {
                return [];
            }

            subjectArray = Ext4.String.trim(subjectArray);
            subjectArray = subjectArray.replace(/[\s,;]+/g, ';');
            subjectArray = subjectArray.replace(/(^;|;$)/g, '');

            if (subjectArray) {
                subjectArray = subjectArray.split(';');
            }
            else {
                subjectArray = [];
            }

            if (subjectArray.length > 0) {
                if (!preserveDuplicates) {
                    subjectArray = Ext4.unique(subjectArray);
                }
                if (!unsorted) {
                    subjectArray.sort();
                }
            }

            return subjectArray;
        },

        singleSubjectLoadReport: function (tab, callback, panel, forceRefresh) {
            var subjectArray = NIRC_EHR.Utils.splitIds(this.down('#subjArea').getValue());  // Don't lowercase the subject Ids

            if (subjectArray.length > 0) {
                subjectArray = Ext4.unique(subjectArray);
            }

            this.subjects = subjectArray;
            this.aliases = {};
            if (Ext4.isDefined(this.aliasTable)) {
                this.getAlias(subjectArray, callback, panel, tab, forceRefresh);
            }
            else {
                callback.call(panel, this.handleFilters(tab, this.subjects), forceRefresh);
            }
        },

        showFlagPopup: function (id, el) {
            var ctx = EHR.Utils.getEHRContext() || {};
            Ext4.create('Ext.window.Window', {
                title: 'Flag Details: ' + id,
                width: 880,
                modal: true,
                bodyStyle: 'padding: 5px;',
                items: [{
                    xtype: 'grid',
                    cls: 'ldk-grid', //variable row height
                    border: false,
                    store: {
                        type: 'labkey-store',
                        containerPath: ctx['EHRStudyContainer'],
                        schemaName: 'study',
                        queryName: 'flags',
                        columns: 'Id,date,enddate,flag/category,flag/value,remark,performedby/displayName',
                        filterArray: [LABKEY.Filter.create('Id', id), LABKEY.Filter.create('isActive', true)],
                        autoLoad: true
                    },
                    viewConfig: {
                        //loadMask: !(Ext4.isIE && Ext4.ieVersion <= 8)
                    },
                    columns: [{
                        header: 'Category',
                        dataIndex: 'flag/category',
                        width: 160
                    }, {
                        header: 'Meaning',
                        dataIndex: 'flag/value',
                        tdCls: 'ldk-wrap-text',
                        width: 160
                    }, {
                        header: 'Date Added',
                        dataIndex: 'date',
                        xtype: 'datecolumn',
                        format: LABKEY.extDefaultDateFormat,
                        width: 110
                    }, {
                        header: 'Date Removed',
                        dataIndex: 'enddate',
                        xtype: 'datecolumn',
                        format: LABKEY.extDefaultDateFormat,
                        width: 110
                    }, {
                        header: 'Remark',
                        dataIndex: 'remark',
                        tdCls: 'ldk-wrap-text',
                        width: 210
                    }, {
                        header: 'Performed By',
                        dataIndex: 'performedby/displayName',
                        width: 110
                    }],
                    border: false
                }],
                buttons: [{
                    text: 'Close',
                    handler: function (btn) {
                        btn.up('window').close();
                    }
                }]
            }).show(el);
        },
    }
}
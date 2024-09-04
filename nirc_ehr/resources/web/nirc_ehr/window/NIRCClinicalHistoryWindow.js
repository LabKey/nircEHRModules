Ext4.define('NIRC_EHR.window.ClinicalHistoryWindow', {
    extend: 'EHR.window.ClinicalHistoryWindow',

    statics: {
        showClinicalHistory: function(objectId, Id, date, el){
            var ctx = EHR.Utils.getEHRContext();
            LDK.Assert.assertNotEmpty('EHRContext not loaded.  This might indicate a ClientDependency issue', ctx);
            if (!ctx){
                return;
            }

            Ext4.create('NIRC_EHR.window.ClinicalHistoryWindow', {
                subjectId: Id,
                containerPath: ctx['EHRStudyContainer']
            }).show(el);
        }
    },

    getButtonCfg: function(){
        var ret = [{
            text: 'Close',
            handler: function(btn){
                btn.up('window').close();
            }
        },{
            text: 'Full Screen',
            scope: this,
            handler: function(btn){
                window.open(LABKEY.ActionURL.buildURL('ehr', 'animalHistory', this.containerPath) + '#subjects:' + this.subjectId + '&inputType:singleSubject&showReport:1&activeReport:clinicalHistory', '_blank');
                btn.up('window').close();
            }
        }];

        // if (EHR.Security.hasLoaded()){
        //     ret.push({
        //         text: 'Actions',
        //         menu: NIRC_EHR.panel.ClinicalManagementPanel.getActionMenu(this.subjectId, this.caseId)
        //     });
        // }

        return ret;
    }
});
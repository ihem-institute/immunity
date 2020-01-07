<?xml version="1.0" encoding="UTF-8"?>
<!-- generated with COPASI 4.15 (Build 95) (http://www.copasi.org) at 2017-07-26 17:21:36 UTC -->
<?oxygen RNGSchema="http://www.copasi.org/static/schema/CopasiML.rng" type="xml"?>
<COPASI xmlns="http://www.copasi.org/static/schema" versionMajor="4" versionMinor="15" versionDevel="95" copasiSourcesModified="0">
  <ListOfFunctions>
    <Function key="Function_13" name="Mass action (irreversible)" type="MassAction" reversible="false">
      <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
   <rdf:Description rdf:about="#Function_13">
   <CopasiMT:is rdf:resource="urn:miriam:obo.sbo:SBO:0000041" />
   </rdf:Description>
   </rdf:RDF>
      </MiriamAnnotation>
      <Comment>
        <body xmlns="http://www.w3.org/1999/xhtml">
<b>Mass action rate law for first order irreversible reactions</b>
<p>
Reaction scheme where the products are created from the reactants and the change of a product quantity is proportional to the product of reactant activities. The reaction scheme does not include any reverse process that creates the reactants from the products. The change of a product quantity is proportional to the quantity of one reactant.
</p>
</body>
      </Comment>
      <Expression>
        k1*PRODUCT&lt;substrate_i&gt;
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_81" name="k1" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_79" name="substrate" order="1" role="substrate"/>
      </ListOfParameterDescriptions>
    </Function>
  </ListOfFunctions>
  <Model key="Model_3" name="LANCL2Activation" simulationType="time" timeUnit="s" volumeUnit="l" areaUnit="m²" lengthUnit="m" quantityUnit="mol" type="deterministic" avogadroConstant="6.02214179e+023">
    <MiriamAnnotation>
<rdf:RDF
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Model_3">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2016-02-16T14:32:21Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>

    </MiriamAnnotation>
    <ListOfUnsupportedAnnotations>
      <UnsupportedAnnotation name="http://www.sbml.org/2001/ns/celldesigner">
<celldesigner:extension xmlns:celldesigner="http://www.sbml.org/2001/ns/celldesigner">
  <celldesigner:modelVersion>4.0</celldesigner:modelVersion>
  <celldesigner:modelDisplay sizeX="1000" sizeY="1000" />
  <celldesigner:listOfCompartmentAliases />
  <celldesigner:listOfComplexSpeciesAliases />
  <celldesigner:listOfSpeciesAliases>
    <celldesigner:speciesAlias id="sa1" species="s1">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="424.0" y="119.0" w="80.0" h="50.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="50.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffffffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa2" species="s2">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="596.0" y="123.0" w="80.0" h="50.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="50.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffffffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa3" species="s3">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="784.0" y="176.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe8e7e7" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa4" species="s3">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="848.0" y="264.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa5" species="s4">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="789.75" y="323.75" w="36.5" h="40.5" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="36.5" height="40.5" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ff9999ff" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa6" species="s4">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="894.0" y="324.0" w="36.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="36.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffececfc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa7" species="s5">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="652.0" y="232.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa8" species="s6">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="685.0" y="355.5" w="70.0" h="25.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="70.0" height="25.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ff99ffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa9" species="s7">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="768.0" y="408.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa11" species="s7">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="592.0" y="408.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe8e9e8" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa12" species="s8">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="768.0" y="472.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa13" species="s8">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="592.0" y="472.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe3e4e3" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa14" species="s9">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="768.0" y="532.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa15" species="s10">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="880.0" y="435.0" w="80.0" h="50.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="50.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffffffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa16" species="s11">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="372.0" y="208.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa17" species="s12">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="140.0" y="364.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa18" species="s13">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="140.0" y="447.0" w="80.0" h="50.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="50.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffffffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa19" species="s14">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="709.0" y="91.5" w="70.0" h="25.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="70.0" height="25.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ff99ffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa20" species="s15">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="488.0" y="385.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa21" species="s16">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="488.0" y="312.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa22" species="s16">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="488.0" y="208.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffdedfde" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa23" species="s9">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="592.0" y="532.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe4e6e4" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa24" species="s15">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="332.0" y="385.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe1e2e1" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa25" species="s1">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="256.0" y="119.0" w="80.0" h="50.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="50.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe8e8e7" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa26" species="s17">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="536.0" y="661.0" w="80.0" h="30.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="30.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffcc99ff" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa27" species="s11">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="188.0" y="208.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe2e3e2" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa28" species="s12">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="141.0" y="281.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe3e4e3" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa29" species="s18">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="488.0" y="577.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa30" species="s18">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="488.0" y="460.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe6e7e6" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa31" species="s19">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="33.0" y="311.5" w="70.0" h="25.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="70.0" height="25.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffff6666" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="0.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa32" species="s24">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="376.0" y="552.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa33" species="s20">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="28.0" y="500.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa34" species="s21">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="244.0" y="368.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa35" species="s22">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="372.0" y="448.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa36" species="s23">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="248.0" y="500.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffccffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa37" species="s20">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="28.0" y="380.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe1e2e1" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa38" species="s24">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="248.0" y="552.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe1e2e1" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa39" species="s23">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="372.0" y="500.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe1e2e1" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa40" species="s22">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="248.0" y="448.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe1e2e1" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa41" species="s21">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="244.0" y="268.0" w="80.0" h="40.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="40.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ffe1e2e1" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
    <celldesigner:speciesAlias id="sa42" species="s25">
      <celldesigner:activity>inactive</celldesigner:activity>
      <celldesigner:bounds x="429.0" y="259.5" w="70.0" h="25.0" />
      <celldesigner:font size="12" />
      <celldesigner:view state="usual" />
      <celldesigner:usualView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="70.0" height="25.0" />
        <celldesigner:singleLine width="1.0" />
        <celldesigner:paint color="ff99ffcc" scheme="Color" />
      </celldesigner:usualView>
      <celldesigner:briefView>
        <celldesigner:innerPosition x="0.0" y="0.0" />
        <celldesigner:boxSize width="80.0" height="60.0" />
        <celldesigner:singleLine width="0.0" />
        <celldesigner:paint color="3fff0000" scheme="Color" />
      </celldesigner:briefView>
      <celldesigner:info state="empty" angle="-1.5707963267948966" />
    </celldesigner:speciesAlias>
  </celldesigner:listOfSpeciesAliases>
  <celldesigner:listOfGroups />
  <celldesigner:listOfProteins>
    <celldesigner:protein id="pr1" name="CX3CR1" type="RECEPTOR" />
    <celldesigner:protein id="pr2" name="DAP12" type="RECEPTOR" />
    <celldesigner:protein id="pr3" name="ERK" type="GENERIC" />
    <celldesigner:protein id="pr4" name="AC" type="GENERIC" />
    <celldesigner:protein id="pr5" name="PKA" type="GENERIC" />
    <celldesigner:protein id="pr6" name="CREB" type="GENERIC" />
    <celldesigner:protein id="pr7" name="IL10" type="GENERIC" />
    <celldesigner:protein id="pr8" name="IL10R" type="RECEPTOR" />
    <celldesigner:protein id="pr9" name="Akt" type="GENERIC" />
    <celldesigner:protein id="pr10" name="M-CSF" type="GENERIC" />
    <celldesigner:protein id="pr11" name="CSF1R" type="RECEPTOR" />
    <celldesigner:protein id="pr12" name="NFAT" type="GENERIC" />
    <celldesigner:protein id="pr13" name="LANCL2" type="GENERIC" />
    <celldesigner:protein id="pr14" name="IL1B" type="GENERIC" />
    <celldesigner:protein id="pr15" name="Traf2" type="GENERIC" />
    <celldesigner:protein id="pr16" name="Fbxo7" type="GENERIC" />
    <celldesigner:protein id="pr17" name="NCOR2" type="GENERIC" />
    <celldesigner:protein id="pr18" name="FOXP1" type="GENERIC" />
    <celldesigner:protein id="pr19" name="NFkB" type="GENERIC" />
  </celldesigner:listOfProteins>
  <celldesigner:listOfGenes />
  <celldesigner:listOfRNAs />
  <celldesigner:listOfAntisenseRNAs />
  <celldesigner:listOfLayers />
  <celldesigner:listOfBlockDiagrams />
</celldesigner:extension>
      </UnsupportedAnnotation>
    </ListOfUnsupportedAnnotations>
    <ListOfCompartments>
      <Compartment key="Compartment_1" name="default" simulationType="fixed" dimensionality="3">
      </Compartment>
    </ListOfCompartments>
    <ListOfMetabolites>
      <Metabolite key="Metabolite_1" name="LANCL2" simulationType="reactions" compartment="Compartment_1">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_1">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2016-02-17T15:47:30Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfUnsupportedAnnotations>
          <UnsupportedAnnotation name="http://www.sbml.org/2001/ns/celldesigner">
<celldesigner:extension xmlns:celldesigner="http://www.sbml.org/2001/ns/celldesigner">
  <celldesigner:positionToCompartment>inside</celldesigner:positionToCompartment>
  <celldesigner:speciesIdentity>
    <celldesigner:class>PROTEIN</celldesigner:class>
    <celldesigner:proteinReference>pr13</celldesigner:proteinReference>
  </celldesigner:speciesIdentity>
  <celldesigner:listOfCatalyzedReactions>
    <celldesigner:catalyzed reaction="re3" />
    <celldesigner:catalyzed reaction="re16" />
    <celldesigner:catalyzed reaction="re25" />
    <celldesigner:catalyzed reaction="re27" />
  </celldesigner:listOfCatalyzedReactions>
</celldesigner:extension>
          </UnsupportedAnnotation>
        </ListOfUnsupportedAnnotations>
      </Metabolite>
      <Metabolite key="Metabolite_3" name="ABA" simulationType="fixed" compartment="Compartment_1">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_3">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2016-02-16T14:56:02Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfUnsupportedAnnotations>
          <UnsupportedAnnotation name="http://www.sbml.org/2001/ns/celldesigner">
<celldesigner:extension xmlns:celldesigner="http://www.sbml.org/2001/ns/celldesigner">
  <celldesigner:positionToCompartment>inside</celldesigner:positionToCompartment>
  <celldesigner:speciesIdentity>
    <celldesigner:class>SIMPLE_MOLECULE</celldesigner:class>
    <celldesigner:name>ABA</celldesigner:name>
  </celldesigner:speciesIdentity>
  <celldesigner:listOfCatalyzedReactions>
    <celldesigner:catalyzed reaction="re18" />
  </celldesigner:listOfCatalyzedReactions>
</celldesigner:extension>
          </UnsupportedAnnotation>
        </ListOfUnsupportedAnnotations>
      </Metabolite>
      <Metabolite key="Metabolite_5" name="pLANCL2" simulationType="reactions" compartment="Compartment_1">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_5">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2016-02-16T14:53:15Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_7" name="LANCL2cyto" simulationType="reactions" compartment="Compartment_1">
      </Metabolite>
    </ListOfMetabolites>
    <ListOfReactions>
      <Reaction key="Reaction_0" name="LANCL2 activation" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_0">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2016-02-16T14:33:26Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfUnsupportedAnnotations>
          <UnsupportedAnnotation name="http://www.sbml.org/2001/ns/celldesigner">
<celldesigner:extension xmlns:celldesigner="http://www.sbml.org/2001/ns/celldesigner">
  <celldesigner:reactionType>STATE_TRANSITION</celldesigner:reactionType>
  <celldesigner:baseReactants>
    <celldesigner:baseReactant species="s16" alias="sa22" />
  </celldesigner:baseReactants>
  <celldesigner:baseProducts>
    <celldesigner:baseProduct species="s16" alias="sa21" />
  </celldesigner:baseProducts>
  <celldesigner:connectScheme connectPolicy="square" rectangleIndex="1">
    <celldesigner:listOfLineDirection>
      <celldesigner:lineDirection index="0" value="vertical" />
      <celldesigner:lineDirection index="1" value="horizontal" />
      <celldesigner:lineDirection index="2" value="vertical" />
    </celldesigner:listOfLineDirection>
  </celldesigner:connectScheme>
  <celldesigner:editPoints>0.48076923076923084,-8.881784197001252E-16 0.48076923076923084,-8.881784197001252E-16</celldesigner:editPoints>
  <celldesigner:line width="1.0" color="ff000000" />
  <celldesigner:listOfModification>
    <celldesigner:modification type="CATALYSIS" modifiers="s25" aliases="sa42" targetLineIndex="-1,6" editPoints="0.061148998003409005,0.3276275560600519 0.8474551325475321,0.1808699608518678">
      <celldesigner:connectScheme connectPolicy="direct">
        <celldesigner:listOfLineDirection>
          <celldesigner:lineDirection index="0" value="horizontal" />
          <celldesigner:lineDirection index="1" value="vertical" />
          <celldesigner:lineDirection index="2" value="horizontal" />
        </celldesigner:listOfLineDirection>
      </celldesigner:connectScheme>
      <celldesigner:line width="1.0" color="ff33cc00" />
    </celldesigner:modification>
  </celldesigner:listOfModification>
</celldesigner:extension>
          </UnsupportedAnnotation>
        </ListOfUnsupportedAnnotations>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_5" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_3" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_1" stoichiometry="1"/>
          <Product metabolite="Metabolite_3" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4992" name="k1" value="0.1"/>
        </ListOfConstants>
        <KineticLaw function="Function_13">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_81">
              <SourceParameter reference="Parameter_4992"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_79">
              <SourceParameter reference="Metabolite_5"/>
              <SourceParameter reference="Metabolite_3"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_1" name="LANCL2inactivation" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_1">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2017-06-26T16:17:23Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_1" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_5" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4991" name="k1" value="0.01"/>
        </ListOfConstants>
        <KineticLaw function="Function_13">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_81">
              <SourceParameter reference="Parameter_4991"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_79">
              <SourceParameter reference="Metabolite_1"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_2" name="LANCL2release" reversible="false" fast="false">
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_1" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_7" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5016" name="k1" value="0.02"/>
        </ListOfConstants>
        <KineticLaw function="Function_13">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_81">
              <SourceParameter reference="Parameter_5016"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_79">
              <SourceParameter reference="Metabolite_1"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
    </ListOfReactions>
    <ListOfModelParameterSets activeSet="ModelParameterSet_1">
      <ModelParameterSet key="ModelParameterSet_1" name="Initial State">
        <ModelParameterGroup cn="String=Initial Time" type="Group">
          <ModelParameter cn="CN=Root,Model=LANCL2Activation" value="0" type="Model" simulationType="time"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Compartment Sizes" type="Group">
          <ModelParameter cn="CN=Root,Model=LANCL2Activation,Vector=Compartments[default]" value="1" type="Compartment" simulationType="fixed"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Species Values" type="Group">
          <ModelParameter cn="CN=Root,Model=LANCL2Activation,Vector=Compartments[default],Vector=Metabolites[LANCL2]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=LANCL2Activation,Vector=Compartments[default],Vector=Metabolites[ABA]" value="6.02214179e+024" type="Species" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=LANCL2Activation,Vector=Compartments[default],Vector=Metabolites[pLANCL2]" value="6.02214179e+023" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=LANCL2Activation,Vector=Compartments[default],Vector=Metabolites[LANCL2cyto]" value="0" type="Species" simulationType="reactions"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Global Quantities" type="Group">
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Kinetic Parameters" type="Group">
          <ModelParameterGroup cn="CN=Root,Model=LANCL2Activation,Vector=Reactions[LANCL2 activation]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=LANCL2Activation,Vector=Reactions[LANCL2 activation],ParameterGroup=Parameters,Parameter=k1" value="0.1" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=LANCL2Activation,Vector=Reactions[LANCL2inactivation]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=LANCL2Activation,Vector=Reactions[LANCL2inactivation],ParameterGroup=Parameters,Parameter=k1" value="0.01" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=LANCL2Activation,Vector=Reactions[LANCL2release]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=LANCL2Activation,Vector=Reactions[LANCL2release],ParameterGroup=Parameters,Parameter=k1" value="0.02" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
        </ModelParameterGroup>
      </ModelParameterSet>
    </ListOfModelParameterSets>
    <StateTemplate>
      <StateTemplateVariable objectReference="Model_3"/>
      <StateTemplateVariable objectReference="Metabolite_1"/>
      <StateTemplateVariable objectReference="Metabolite_5"/>
      <StateTemplateVariable objectReference="Metabolite_7"/>
      <StateTemplateVariable objectReference="Metabolite_3"/>
      <StateTemplateVariable objectReference="Compartment_1"/>
    </StateTemplate>
    <InitialState type="initialState">
      0 0 6.02214179e+023 0 6.02214179e+024 1 
    </InitialState>
  </Model>
  <ListOfTasks>
    <Task key="Task_14" name="Steady-State" type="steadyState" scheduled="false" updateModel="false">
      <Report reference="Report_9" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="JacobianRequested" type="bool" value="1"/>
        <Parameter name="StabilityAnalysisRequested" type="bool" value="1"/>
      </Problem>
      <Method name="Enhanced Newton" type="EnhancedNewton">
        <Parameter name="Resolution" type="unsignedFloat" value="1e-009"/>
        <Parameter name="Derivation Factor" type="unsignedFloat" value="0.001"/>
        <Parameter name="Use Newton" type="bool" value="1"/>
        <Parameter name="Use Integration" type="bool" value="1"/>
        <Parameter name="Use Back Integration" type="bool" value="1"/>
        <Parameter name="Accept Negative Concentrations" type="bool" value="0"/>
        <Parameter name="Iteration Limit" type="unsignedInteger" value="50"/>
        <Parameter name="Maximum duration for forward integration" type="unsignedFloat" value="1000000000"/>
        <Parameter name="Maximum duration for backward integration" type="unsignedFloat" value="1000000"/>
      </Method>
    </Task>
    <Task key="Task_15" name="Time-Course" type="timeCourse" scheduled="false" updateModel="false">
      <Problem>
        <Parameter name="StepNumber" type="unsignedInteger" value="500"/>
        <Parameter name="StepSize" type="float" value="0.1"/>
        <Parameter name="Duration" type="float" value="50"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="1"/>
        <Parameter name="OutputStartTime" type="float" value="0"/>
        <Parameter name="Output Event" type="bool" value="0"/>
        <Parameter name="Continue on Simultaneous Events" type="bool" value="1"/>
      </Problem>
      <Method name="Deterministic (LSODA)" type="Deterministic(LSODA)">
        <Parameter name="Integrate Reduced Model" type="bool" value="0"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="1e-006"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="1e-012"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="10000"/>
      </Method>
    </Task>
    <Task key="Task_16" name="Scan" type="scan" scheduled="false" updateModel="true">
      <Problem>
        <Parameter name="Subtask" type="unsignedInteger" value="1"/>
        <ParameterGroup name="ScanItems">
          <ParameterGroup name="ScanItem">
            <Parameter name="Maximum" type="float" value="10"/>
            <Parameter name="Minimum" type="float" value="0"/>
            <Parameter name="Number of steps" type="unsignedInteger" value="10"/>
            <Parameter name="Object" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Compartments[default],Vector=Metabolites[IL10],Reference=InitialConcentration"/>
            <Parameter name="Type" type="unsignedInteger" value="1"/>
            <Parameter name="log" type="bool" value="0"/>
          </ParameterGroup>
          <ParameterGroup name="ScanItem">
            <Parameter name="Maximum" type="float" value="10"/>
            <Parameter name="Minimum" type="float" value="0"/>
            <Parameter name="Number of steps" type="unsignedInteger" value="10"/>
            <Parameter name="Object" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Compartments[default],Vector=Metabolites[IFNg],Reference=InitialConcentration"/>
            <Parameter name="Type" type="unsignedInteger" value="1"/>
            <Parameter name="log" type="bool" value="0"/>
          </ParameterGroup>
        </ParameterGroup>
        <Parameter name="Output in subtask" type="bool" value="1"/>
        <Parameter name="Adjust initial conditions" type="bool" value="0"/>
      </Problem>
      <Method name="Scan Framework" type="ScanFramework">
      </Method>
    </Task>
    <Task key="Task_17" name="Elementary Flux Modes" type="fluxMode" scheduled="false" updateModel="false">
      <Report reference="Report_10" target="" append="1" confirmOverwrite="1"/>
      <Problem>
      </Problem>
      <Method name="EFM Algorithm" type="EFMAlgorithm">
      </Method>
    </Task>
    <Task key="Task_18" name="Optimization" type="optimization" scheduled="false" updateModel="false">
      <Report reference="Report_11" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Subtask" type="cn" value="CN=Root,Vector=TaskList[Sensitivities]"/>
        <ParameterText name="ObjectiveExpression" type="expression">
          &lt;TaskList[Sensitivities].Scaled sensitivities array[.]&gt;
        </ParameterText>
        <Parameter name="Maximize" type="bool" value="0"/>
        <Parameter name="Randomize Start Values" type="bool" value="0"/>
        <Parameter name="Calculate Statistics" type="bool" value="1"/>
        <ParameterGroup name="OptimizationItemList">
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.001"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Akt activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.01779"/>
            <Parameter name="UpperBound" type="cn" value="0.1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Akt activation],ParameterGroup=Parameters,Parameter=K2,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.11105"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Akt activation],ParameterGroup=Parameters,Parameter=K3,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1328"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Akt activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1115"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Akt activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1922"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Akt activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.136"/>
            <Parameter name="UpperBound" type="cn" value="1.5"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CREB activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.0635"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CREB activation],ParameterGroup=Parameters,Parameter=K2,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.0728"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CREB activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1493"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CREB activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.19685"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CREB activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.508"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CSF1R activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.36647"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CSF1R activation],ParameterGroup=Parameters,Parameter=K2,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.0551"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CSF1R activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.185"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CSF1R activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.2"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CSF1R activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.907"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CX3CL1 activation],ParameterGroup=Parameters,Parameter=B,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CX3CL1 activation],ParameterGroup=Parameters,Parameter=K,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.0678"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CX3CL1 activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.126"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CX3CL1 activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CX3CL1 degradation],ParameterGroup=Parameters,Parameter=k1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CX3CR1 activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.19115"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CX3CR1 activation],ParameterGroup=Parameters,Parameter=K2,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.05"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CX3CR1 activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.13385"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CX3CR1 activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1715"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[CX3CR1 activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.6325"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Ca2+ transport],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.3076"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Ca2+ transport],ParameterGroup=Parameters,Parameter=K2,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1292"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.001"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[DAP12 activation],ParameterGroup=Parameters,Parameter=k1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.0384359"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[DAP12 degradation],ParameterGroup=Parameters,Parameter=k1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[ERK activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1043"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[ERK activation],ParameterGroup=Parameters,Parameter=K2,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.0851"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[ERK activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.0487012"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[ERK activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1607"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[ERK activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.0115"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[FOXP1 activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.08359999999999999"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[FOXP1 activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.10955"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[FOXP1 activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.31685"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[FOXP1 activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.193"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Fbxo7 activation],ParameterGroup=Parameters,Parameter=B,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Fbxo7 activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1536"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Fbxo7 activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1235"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Fbxo7 activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.19115"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Fbxo7 activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.349"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IL10 degradation],ParameterGroup=Parameters,Parameter=k1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IL10 production],ParameterGroup=Parameters,Parameter=K,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.0536"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.001"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IL10 production],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.0593"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IL10 production],ParameterGroup=Parameters,Parameter=K2,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.09815"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.001"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IL10 production],ParameterGroup=Parameters,Parameter=K3,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.01589"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.1"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IL10 production],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.66808"/>
            <Parameter name="UpperBound" type="cn" value="10"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IL10 production],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.193"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IL10R activation],ParameterGroup=Parameters,Parameter=B,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IL10R activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.463615"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IL10R activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.169469"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IL10R activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.188"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IL10R activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.042"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IFNg degradation],ParameterGroup=Parameters,Parameter=k1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IFNg production],ParameterGroup=Parameters,Parameter=K,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.06035"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IFNg production],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.06755"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IFNg production],ParameterGroup=Parameters,Parameter=K2,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.0785"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IFNg production],ParameterGroup=Parameters,Parameter=K3,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.0692"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IFNg production],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.467575"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[IFNg production],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.151"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[KLF4 activation],ParameterGroup=Parameters,Parameter=B,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[KLF4 activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.07430000000000001"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[KLF4 activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[KLF4 activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.114"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[KLF4 activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.1"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[LANCL2 activation],ParameterGroup=Parameters,Parameter=B,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[LANCL2 activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.06395000000000001"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[LANCL2 activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1328"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[LANCL2 activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1835"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[LANCL2 activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.643"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[M-CSF degradation],ParameterGroup=Parameters,Parameter=k1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[M-CSF production],ParameterGroup=Parameters,Parameter=B,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[M-CSF production],ParameterGroup=Parameters,Parameter=K,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.13325"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[M-CSF production],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.03888"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[M-CSF production],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.049"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Mreg differentiation],ParameterGroup=Parameters,Parameter=K,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.49275"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Mreg differentiation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.08075"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Mreg differentiation],ParameterGroup=Parameters,Parameter=K2,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1589"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Mreg differentiation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.001"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Mreg differentiation],ParameterGroup=Parameters,Parameter=exp2,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="2.428"/>
            <Parameter name="UpperBound" type="cn" value="4"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Mreg differentiation],ParameterGroup=Parameters,Parameter=exp3,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.001"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NCOR2 activation],ParameterGroup=Parameters,Parameter=B,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NCOR2 activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.05315"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NCOR2 activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.124465"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NCOR2 activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.59075"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NCOR2 activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.8185"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NFAT activation],ParameterGroup=Parameters,Parameter=B,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NFAT activation],ParameterGroup=Parameters,Parameter=K,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1307"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NFAT activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1586"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NFAT activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.11105"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NFAT activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.18755"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NFAT activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.5755"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NFkB activation],ParameterGroup=Parameters,Parameter=B,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NFkB activation],ParameterGroup=Parameters,Parameter=K,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.316437"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.001"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NFkB activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.015346"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NFkB activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.139789"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.1"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NFkB activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.481511"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[NFkB activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.73699"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[PKA activation],ParameterGroup=Parameters,Parameter=B,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[PKA activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.0515"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[PKA activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1199"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[PKA activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.18635"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[PKA activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.301"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Traf2 activation],ParameterGroup=Parameters,Parameter=K,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.09544999999999999"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.001"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Traf2 activation],ParameterGroup=Parameters,Parameter=K1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.06815"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.001"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Traf2 activation],ParameterGroup=Parameters,Parameter=K2,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.01646"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Traf2 activation],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.984949"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.1"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Traf2 activation],ParameterGroup=Parameters,Parameter=Vr,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.262"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[Traf2 activation],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.493"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.05"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[cAMP degradation],ParameterGroup=Parameters,Parameter=k1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.1"/>
            <Parameter name="UpperBound" type="cn" value="0.5"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[cAMP production],ParameterGroup=Parameters,Parameter=K,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.185"/>
            <Parameter name="UpperBound" type="cn" value="1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.01"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[cAMP production],ParameterGroup=Parameters,Parameter=Vf,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="0.029"/>
            <Parameter name="UpperBound" type="cn" value="0.1"/>
          </ParameterGroup>
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.5"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Reactions[cAMP production],ParameterGroup=Parameters,Parameter=exp1,Reference=Value"/>
            <Parameter name="StartValue" type="float" value="1.0895"/>
            <Parameter name="UpperBound" type="cn" value="2"/>
          </ParameterGroup>
        </ParameterGroup>
        <ParameterGroup name="OptimizationConstraintList">
        </ParameterGroup>
      </Problem>
      <Method name="Particle Swarm" type="ParticleSwarm">
        <Parameter name="Iteration Limit" type="unsignedInteger" value="2000"/>
        <Parameter name="Swarm Size" type="unsignedInteger" value="50"/>
        <Parameter name="Std. Deviation" type="unsignedFloat" value="1e-006"/>
        <Parameter name="Random Number Generator" type="unsignedInteger" value="1"/>
        <Parameter name="Seed" type="unsignedInteger" value="0"/>
      </Method>
    </Task>
    <Task key="Task_19" name="Parameter Estimation" type="parameterFitting" scheduled="false" updateModel="false">
      <Report reference="Report_12" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Maximize" type="bool" value="0"/>
        <Parameter name="Randomize Start Values" type="bool" value="0"/>
        <Parameter name="Calculate Statistics" type="bool" value="1"/>
        <ParameterGroup name="OptimizationItemList">
        </ParameterGroup>
        <ParameterGroup name="OptimizationConstraintList">
        </ParameterGroup>
        <Parameter name="Steady-State" type="cn" value="CN=Root,Vector=TaskList[Steady-State]"/>
        <Parameter name="Time-Course" type="cn" value="CN=Root,Vector=TaskList[Time-Course]"/>
        <Parameter name="Create Parameter Sets" type="bool" value="0"/>
        <ParameterGroup name="Experiment Set">
        </ParameterGroup>
        <ParameterGroup name="Validation Set">
          <Parameter name="Threshold" type="unsignedInteger" value="5"/>
          <Parameter name="Weight" type="unsignedFloat" value="1"/>
        </ParameterGroup>
      </Problem>
      <Method name="Evolutionary Programming" type="EvolutionaryProgram">
        <Parameter name="Number of Generations" type="unsignedInteger" value="200"/>
        <Parameter name="Population Size" type="unsignedInteger" value="20"/>
        <Parameter name="Random Number Generator" type="unsignedInteger" value="1"/>
        <Parameter name="Seed" type="unsignedInteger" value="0"/>
      </Method>
    </Task>
    <Task key="Task_20" name="Metabolic Control Analysis" type="metabolicControlAnalysis" scheduled="false" updateModel="false">
      <Report reference="Report_13" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Steady-State" type="key" value="Task_14"/>
      </Problem>
      <Method name="MCA Method (Reder)" type="MCAMethod(Reder)">
        <Parameter name="Modulation Factor" type="unsignedFloat" value="1e-009"/>
        <Parameter name="Use Reeder" type="bool" value="1"/>
        <Parameter name="Use Smallbone" type="bool" value="1"/>
      </Method>
    </Task>
    <Task key="Task_21" name="Lyapunov Exponents" type="lyapunovExponents" scheduled="false" updateModel="false">
      <Report reference="Report_14" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="ExponentNumber" type="unsignedInteger" value="3"/>
        <Parameter name="DivergenceRequested" type="bool" value="1"/>
        <Parameter name="TransientTime" type="float" value="0"/>
      </Problem>
      <Method name="Wolf Method" type="WolfMethod">
        <Parameter name="Orthonormalization Interval" type="unsignedFloat" value="1"/>
        <Parameter name="Overall time" type="unsignedFloat" value="1000"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="1e-006"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="1e-012"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="10000"/>
      </Method>
    </Task>
    <Task key="Task_22" name="Time Scale Separation Analysis" type="timeScaleSeparationAnalysis" scheduled="false" updateModel="false">
      <Report reference="Report_15" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="StepNumber" type="unsignedInteger" value="100"/>
        <Parameter name="StepSize" type="float" value="0.01"/>
        <Parameter name="Duration" type="float" value="1"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="1"/>
        <Parameter name="OutputStartTime" type="float" value="0"/>
      </Problem>
      <Method name="ILDM (LSODA,Deuflhard)" type="TimeScaleSeparation(ILDM,Deuflhard)">
        <Parameter name="Deuflhard Tolerance" type="unsignedFloat" value="1e-006"/>
      </Method>
    </Task>
    <Task key="Task_23" name="Sensitivities" type="sensitivities" scheduled="false" updateModel="false">
      <Report reference="Report_16" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="SubtaskType" type="unsignedInteger" value="2"/>
        <ParameterGroup name="TargetFunctions">
          <Parameter name="SingleObject" type="cn" value="CN=Root,Model=LANCL2Activation,Vector=Compartments[default],Vector=Metabolites[Mreg],Reference=Concentration"/>
          <Parameter name="ObjectListType" type="unsignedInteger" value="1"/>
        </ParameterGroup>
        <ParameterGroup name="ListOfVariables">
          <ParameterGroup name="Variables">
            <Parameter name="SingleObject" type="cn" value=""/>
            <Parameter name="ObjectListType" type="unsignedInteger" value="42"/>
          </ParameterGroup>
        </ParameterGroup>
      </Problem>
      <Method name="Sensitivities Method" type="SensitivitiesMethod">
        <Parameter name="Delta factor" type="unsignedFloat" value="0.001"/>
        <Parameter name="Delta minimum" type="unsignedFloat" value="1e-012"/>
      </Method>
    </Task>
    <Task key="Task_24" name="Moieties" type="moieties" scheduled="false" updateModel="false">
      <Problem>
      </Problem>
      <Method name="Householder Reduction" type="Householder">
      </Method>
    </Task>
    <Task key="Task_25" name="Cross Section" type="crosssection" scheduled="false" updateModel="false">
      <Problem>
        <Parameter name="StepNumber" type="unsignedInteger" value="100"/>
        <Parameter name="StepSize" type="float" value="0.01"/>
        <Parameter name="Duration" type="float" value="1"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="1"/>
        <Parameter name="OutputStartTime" type="float" value="0"/>
        <Parameter name="Output Event" type="bool" value="0"/>
        <Parameter name="Continue on Simultaneous Events" type="bool" value="0"/>
        <Parameter name="LimitCrossings" type="bool" value="0"/>
        <Parameter name="NumCrossingsLimit" type="unsignedInteger" value="0"/>
        <Parameter name="LimitOutTime" type="bool" value="0"/>
        <Parameter name="LimitOutCrossings" type="bool" value="0"/>
        <Parameter name="PositiveDirection" type="bool" value="1"/>
        <Parameter name="NumOutCrossingsLimit" type="unsignedInteger" value="0"/>
        <Parameter name="LimitUntilConvergence" type="bool" value="0"/>
        <Parameter name="ConvergenceTolerance" type="float" value="0"/>
        <Parameter name="Threshold" type="float" value="0"/>
        <Parameter name="DelayOutputUntilConvergence" type="bool" value="0"/>
        <Parameter name="OutputConvergenceTolerance" type="float" value="0"/>
        <ParameterText name="TriggerExpression" type="expression">
          
        </ParameterText>
        <Parameter name="SingleVariable" type="cn" value=""/>
      </Problem>
      <Method name="Deterministic (LSODA)" type="Deterministic(LSODA)">
        <Parameter name="Integrate Reduced Model" type="bool" value="0"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="1e-006"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="1e-012"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="10000"/>
      </Method>
    </Task>
    <Task key="Task_26" name="Linear Noise Approximation" type="linearNoiseApproximation" scheduled="false" updateModel="false">
      <Report reference="Report_17" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Steady-State" type="key" value="Task_14"/>
      </Problem>
      <Method name="Linear Noise Approximation" type="LinearNoiseApproximation">
      </Method>
    </Task>
  </ListOfTasks>
  <ListOfReports>
    <Report key="Report_9" name="Steady-State" taskType="steadyState" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Footer>
        <Object cn="CN=Root,Vector=TaskList[Steady-State]"/>
      </Footer>
    </Report>
    <Report key="Report_10" name="Elementary Flux Modes" taskType="fluxMode" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Footer>
        <Object cn="CN=Root,Vector=TaskList[Elementary Flux Modes],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_11" name="Optimization" taskType="optimization" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Object=Description"/>
        <Object cn="String=\[Function Evaluations\]"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="String=\[Best Value\]"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="String=\[Best Parameters\]"/>
      </Header>
      <Body>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Problem=Optimization,Reference=Function Evaluations"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Problem=Optimization,Reference=Best Value"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Problem=Optimization,Reference=Best Parameters"/>
      </Body>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_12" name="Parameter Estimation" taskType="parameterFitting" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Object=Description"/>
        <Object cn="String=\[Function Evaluations\]"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="String=\[Best Value\]"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="String=\[Best Parameters\]"/>
      </Header>
      <Body>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Problem=Parameter Estimation,Reference=Function Evaluations"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Problem=Parameter Estimation,Reference=Best Value"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Problem=Parameter Estimation,Reference=Best Parameters"/>
      </Body>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_13" name="Metabolic Control Analysis" taskType="metabolicControlAnalysis" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Metabolic Control Analysis],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Metabolic Control Analysis],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_14" name="Lyapunov Exponents" taskType="lyapunovExponents" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Lyapunov Exponents],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Lyapunov Exponents],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_15" name="Time Scale Separation Analysis" taskType="timeScaleSeparationAnalysis" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Time Scale Separation Analysis],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Time Scale Separation Analysis],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_16" name="Sensitivities" taskType="sensitivities" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Sensitivities],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Sensitivities],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_17" name="Linear Noise Approximation" taskType="linearNoiseApproximation" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Linear Noise Approximation],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Linear Noise Approximation],Object=Result"/>
      </Footer>
    </Report>
  </ListOfReports>
  <ListOfPlots>
    <PlotSpecification name="Concentrations, Volumes, and Global Quantity Values" type="Plot2D" active="0">
      <Parameter name="log X" type="bool" value="0"/>
      <Parameter name="log Y" type="bool" value="0"/>
      <ListOfPlotItems>
        <PlotItem name="[LANCL2]" type="Curve2D">
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=LANCL2Activation,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=LANCL2Activation,Vector=Compartments[default],Vector=Metabolites[LANCL2],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="[pLANCL2]" type="Curve2D">
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=LANCL2Activation,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=LANCL2Activation,Vector=Compartments[default],Vector=Metabolites[pLANCL2],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
      </ListOfPlotItems>
    </PlotSpecification>
    <PlotSpecification name="plot_1" type="Plot2D" active="0">
      <Parameter name="log X" type="bool" value="0"/>
      <Parameter name="log Y" type="bool" value="0"/>
      <ListOfPlotItems>
      </ListOfPlotItems>
    </PlotSpecification>
    <PlotSpecification name="Concentrations, Volumes, and Global Quantity Values_1" type="Plot2D" active="1">
      <Parameter name="log X" type="bool" value="0"/>
      <Parameter name="log Y" type="bool" value="0"/>
      <ListOfPlotItems>
        <PlotItem name="[LANCL2]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=LANCL2Activation,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=LANCL2Activation,Vector=Compartments[default],Vector=Metabolites[LANCL2],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="[pLANCL2]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=LANCL2Activation,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=LANCL2Activation,Vector=Compartments[default],Vector=Metabolites[pLANCL2],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="[LANCL2cyto]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=LANCL2Activation,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=LANCL2Activation,Vector=Compartments[default],Vector=Metabolites[LANCL2cyto],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
      </ListOfPlotItems>
    </PlotSpecification>
    <PlotSpecification name="plot_2" type="Plot2D" active="0">
      <Parameter name="log X" type="bool" value="0"/>
      <Parameter name="log Y" type="bool" value="0"/>
      <ListOfPlotItems>
      </ListOfPlotItems>
    </PlotSpecification>
    <PlotSpecification name="plot_3" type="Plot2D" active="0">
      <Parameter name="log X" type="bool" value="0"/>
      <Parameter name="log Y" type="bool" value="0"/>
      <ListOfPlotItems>
      </ListOfPlotItems>
    </PlotSpecification>
  </ListOfPlots>
  <GUI>
  </GUI>
  <SBMLReference file="hplancl2.xml">
    <SBMLMap SBMLid="default" COPASIkey="Compartment_1"/>
    <SBMLMap SBMLid="re18" COPASIkey="Reaction_0"/>
    <SBMLMap SBMLid="s16" COPASIkey="Metabolite_1"/>
    <SBMLMap SBMLid="s25" COPASIkey="Metabolite_3"/>
  </SBMLReference>
</COPASI>
